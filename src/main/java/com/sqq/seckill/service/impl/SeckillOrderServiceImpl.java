package com.sqq.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqq.seckill.mapper.SeckillOrderMapper;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.service.ISeckillOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-02
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

}
