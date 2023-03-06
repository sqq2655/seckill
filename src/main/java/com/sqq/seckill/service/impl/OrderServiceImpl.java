package com.sqq.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqq.seckill.exception.GlobeException;
import com.sqq.seckill.mapper.OrderMapper;
import com.sqq.seckill.pojo.Order;
import com.sqq.seckill.pojo.SeckillGoods;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IGoodsService;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.service.ISeckillGoodsService;
import com.sqq.seckill.service.ISeckillOrderService;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.OrderDetailVo;
import com.sqq.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-02
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


    @Autowired
    private ISeckillGoodsService iSeckillGoodsService;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();



        SeckillGoods seckillGoods = iSeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
//        iSeckillGoodsService.updateById(seckillGoods);
//        boolean seckillresult = iSeckillGoodsService.update(new UpdateWrapper<SeckillGoods>().set("stock_count",
//                seckillGoods.getStockCount()).eq("id",seckillGoods.getId()).gt("stock_cont",0));
        boolean result = iSeckillGoodsService.update(new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goodsVo.getId())
                .gt("stock_count", 0)
        );
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
            return null;
        }
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        iSeckillOrderService.save(seckillOrder);


        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodsVo.getId(),seckillOrder);
        return order ;
    }

    /*订单详情*/
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (null == orderId){
            throw new GlobeException(RespBeanEnum.ORDER_NO_EXIST);
        }
        Order order = orderMapper.selectById(orderId);  
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoodsVo(goodsVo);
        orderDetailVo.setOrder(order);
        return orderDetailVo;
    }
}
