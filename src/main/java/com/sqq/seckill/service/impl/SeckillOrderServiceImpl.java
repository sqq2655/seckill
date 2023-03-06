package com.sqq.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqq.seckill.mapper.SeckillOrderMapper;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>

 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId));
        if( null != seckillOrder){
            return seckillOrder.getOrderId();
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }else{
            return  0L;
        }
    }
}
