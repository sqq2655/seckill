package com.sqq.seckill.exception;

import com.sqq.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 17:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobeException extends RuntimeException {

    private RespBeanEnum respBeanEnum;
}
