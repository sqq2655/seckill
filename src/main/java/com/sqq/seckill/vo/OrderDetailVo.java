package com.sqq.seckill.vo;

import com.sqq.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/5 13:28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private Order order;

    private  GoodsVo goodsVo;

}
