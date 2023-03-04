package com.sqq.seckill.controller;

import com.sqq.seckill.service.IUserService;
import com.sqq.seckill.vo.LoginVo;
import com.sqq.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 登录
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

	@Autowired
	private IUserService userService;

	/**
	 * 功能描述: 跳转登录页面
	 */
	@RequestMapping("/toLogin")
	public String toLogin(){
		return "login";
	}

	/**
	 * 功能描述: 登录功能
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
		log.info("{}",loginVo);
		return userService.doLogin(loginVo,request,response);
	}

}