package com.sqq.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqq.seckill.mapper.OrderMapper;
import com.sqq.seckill.pojo.Order;
import com.sqq.seckill.pojo.SeckillGoods;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.service.ISeckillGoodsService;
import com.sqq.seckill.service.ISeckillOrderService;
import com.sqq.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Order seckill(User user, GoodsVo goods) {
        SeckillGoods seckillGoods = iSeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        iSeckillGoodsService.updateById(seckillGoods);


        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        iSeckillOrderService.save(seckillOrder);


        return order ;
    }
}
