package com.sqq.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqq.seckill.pojo.Order;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-02
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}
