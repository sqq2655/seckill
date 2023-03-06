package com.sqq.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqq.seckill.pojo.Order;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.rabbitmq.MQSender;
import com.sqq.seckill.service.IGoodsService;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.service.ISeckillGoodsService;
import com.sqq.seckill.service.ISeckillOrderService;
import com.sqq.seckill.utils.JsonUtil;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.RespBean;
import com.sqq.seckill.vo.RespBeanEnum;
import com.sqq.seckill.vo.SeckillMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 21:41
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    /*
    *  初始化时
    * 在redis标识每个商品是否有库存，减少redis访问
    * */
    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();
    /*
    * windos优化器前QPS 1719，优化后qps 3506
    * Linux优化前QPS：299
    * */
    @RequestMapping( value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if(user ==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否重复抢购，
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        //通过内存标记，减少redis访问
        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);

        }


        // 通过redis预减库存，减少对商品库存访问。
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //redis中库存小于0 直接返回,内存标记未true
        if (stock < 0){
            EmptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //rabbit异步发送下单消息
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));

        return RespBean.success(0);
    }

    /*
     * 获取秒杀结果
     * orderId 成功， -1，秒杀失败；0，秒杀成功。
     * */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId) {
        if (user == null){
             return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = iSeckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);

    }


    /*
    * 初始化时执行一些方法
    *
    * */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> lists = iGoodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(lists)){
            return ;
        }
        for (GoodsVo goodsVo : lists) {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        }

    }



}
