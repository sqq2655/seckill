package com.sqq.seckill.vo;

import com.sqq.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 详情返回对象

 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {

	private User tuser;

	private GoodsVo goodsVo;

	private int secKillStatus;

	private int remainSeconds;
}
