package com.sqq.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqq.seckill.pojo.Goods;
import com.sqq.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
