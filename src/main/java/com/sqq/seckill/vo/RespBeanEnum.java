package com.sqq.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/1 21:50
 */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //登录模块
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    LOGIN_ERROR(500210,"用户名或密码错误"),
    MOBILE_ERROR(500211,"手机号格式不正确"),
    BIND_ERROR(500212,"参数校验异常"),
    MOBILE_NO_EXIST(500213,"手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214,"密码更新失败"),
    SESSION_ERROR(500512,"用户不存在"),

    //秒杀模块
    EMPTY_STOCK(500500,"库存为空"),
    REPEATE_ERROR(500501,"该商品每人限购一件"),
    //订单模块
    ORDER_NO_EXIST(500300,"订单不存在"),

    ;

    private final Integer code;
    private final String message;

}
