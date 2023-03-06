package com.sqq.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-02
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
