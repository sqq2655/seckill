package com.sqq.seckill.exception;

import com.sqq.seckill.vo.RespBean;
import com.sqq.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 17:20
 */

@RestControllerAdvice
public class GlobeExceptionHandler {


    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if (e instanceof GlobeException){
            GlobeException ex = (GlobeException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }else if(e instanceof BindException){
            BindException ex = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常"+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;

        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
