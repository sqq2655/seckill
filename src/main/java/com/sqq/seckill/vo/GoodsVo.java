package com.sqq.seckill.vo;

import com.sqq.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 20:12
 */
/*商品返回对象*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {


    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 库存数量
     */
    private Integer stockCount;

    /**
     * 秒杀开始时间
     */
    private Date startDate;

    /**
     * 秒杀结束时间
     */
    private Date endDate;

}
