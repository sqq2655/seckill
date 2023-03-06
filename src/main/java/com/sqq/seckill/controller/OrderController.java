package com.sqq.seckill.controller;


import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.vo.OrderDetailVo;
import com.sqq.seckill.vo.RespBean;
import com.sqq.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-02
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public RespBean detail(User user, Long orderId){
        System.out.println(orderId);
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo orderDetailVo = iOrderService.detail(orderId);
        return RespBean.success(orderDetailVo);

    }

}
