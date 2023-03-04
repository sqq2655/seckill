package com.sqq.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqq.seckill.pojo.Goods;
import com.sqq.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author zhoubin
 * @since 2023-03-02
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
