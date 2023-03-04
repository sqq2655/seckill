package com.sqq.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/1 21:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;
    /**
     * 成功返回结果
     */
    public static RespBean success() {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),
                RespBeanEnum.SUCCESS.getMessage(), null);
    }
    /**
     * 成功返回结果
     *
     * @param obj
     */
    public static RespBean success(Object obj) {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),
                RespBeanEnum.SUCCESS.getMessage(), obj);
    }
    /**
     * 失败返回结果
     *
     * @param respBeanEnum
     * @return
     */
    public static RespBean error(RespBeanEnum respBeanEnum) {
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(),
                null);
    }
}