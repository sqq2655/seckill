package com.sqq.seckill.controller;


import com.sqq.seckill.pojo.User;
import com.sqq.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /*
    * 测试用户
    * */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){

        return RespBean.success(user);
    }
}
