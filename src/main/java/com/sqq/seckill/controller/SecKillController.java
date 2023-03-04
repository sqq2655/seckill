package com.sqq.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqq.seckill.pojo.Order;
import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IGoodsService;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.service.ISeckillGoodsService;
import com.sqq.seckill.service.ISeckillOrderService;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 21:41
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private ISeckillOrderService iSeckillOrderService;

    @Autowired
    private IOrderService iOrderService;


    /*
    * windos优化器前QPS 1719，
    * Linux优化前QPS：299
    * */
    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user,Long goodsId) {
        if(user ==null){
            return "login";
        }
        model.addAttribute("user",user);
        //判断库存
        GoodsVo goods = iGoodsService.findGoodsVoByGoodsId(goodsId);
        if(goods.getStockCount()<1){
            model.addAttribute("errmsg",RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = iSeckillOrderService.getOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getId())
                .eq("goods_id", goodsId));

        if(seckillOrder != null){
            model.addAttribute("errmsg",RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = iOrderService.seckill(user,goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }
}
