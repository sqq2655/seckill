package com.sqq.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.vo.LoginVo;
import com.sqq.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    //根据cookie 获取用户
    User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
