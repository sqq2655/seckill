package com.sqq.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqq.seckill.pojo.Order;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IGoodsService;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.service.ISeckillGoodsService;
import com.sqq.seckill.service.ISeckillOrderService;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.RespBean;
import com.sqq.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 21:41
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;

    @Autowired
    private IOrderService iOrderService;


    @Autowired
    private RedisTemplate redisTemplate;
    /*
    * windos优化器前QPS 1719，
    * Linux优化前QPS：299
    * */
    @RequestMapping( value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(Model model, User user, Long goodsId) {
        if(user ==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = iGoodsService.findGoodsVoByGoodsId(goodsId);
        if(goods.getStockCount()<1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
//        SeckillOrder seckillOrder = iSeckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
//                .eq("user_id", user.getId())
//                .eq("goods_id", goodsId));
        //通过redis 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        if(seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = iOrderService.seckill(user,goods);

        return RespBean.success(order);
    }
}
