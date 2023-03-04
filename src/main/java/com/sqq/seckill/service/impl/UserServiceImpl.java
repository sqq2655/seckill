package com.sqq.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqq.seckill.exception.GlobeException;
import com.sqq.seckill.mapper.UserMapper;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IUserService;
import com.sqq.seckill.utils.CookieUtil;
import com.sqq.seckill.utils.MD5Util;
import com.sqq.seckill.utils.UUIDUtil;
import com.sqq.seckill.utils.ValidatorUtil;
import com.sqq.seckill.vo.LoginVo;
import com.sqq.seckill.vo.RespBean;
import com.sqq.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = userMapper.selectById(mobile);


        if (null == user){
            throw new GlobeException(RespBeanEnum.LOGIN_ERROR);
        }
        //判断密码是否正确
        if (!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            throw new GlobeException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie，设置cookie
        String ticket = UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket,user);
        redisTemplate.opsForValue().set("user:" + ticket,user);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }


    //修改密码
    public RespBean updatePwd(String userTicket,String password,
                              HttpServletRequest request, HttpServletResponse response) {

        User user = getUserByCookie(userTicket, request, response);
        if  (null == user){
            throw new GlobeException(RespBeanEnum.MOBILE_NO_EXIST);
        }

        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int res = userMapper.updateById(user);
        if (1==res){
            //删除redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }



}
