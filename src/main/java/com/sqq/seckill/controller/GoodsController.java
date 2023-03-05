package com.sqq.seckill.controller;

import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IGoodsService;
import com.sqq.seckill.service.IUserService;
import com.sqq.seckill.vo.DetailVo;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/2 18:16
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /*
    * windows优化前QPS：2487
    *           缓存qps：3710
    *
    * linux优化前QPS： 871
    * */
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,HttpServletRequest request,HttpServletResponse response){

        //将页面缓存到redis
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        List<GoodsVo>  goodList = iGoodsService.findGoodsVo();
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodList);

        //如果redis为空，手动渲染
        WebContext context = new WebContext(request, response,request.getServletContext()
                                                ,request.getLocale(),model.asMap());
         html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,60, TimeUnit.SECONDS);
        }
        return html;

    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public RespBean detail(User user, @PathVariable Long goodsId,
                             HttpServletRequest request, HttpServletResponse response){


        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date newDate = new Date();
        int secKillStatus = 0;
        int remainSeconds = 0;
        //秒杀未开始
        if (newDate.before(startDate)) {
            secKillStatus = 0;
            remainSeconds = ((int)((startDate.getTime()-newDate.getTime())/1000));
        //秒杀结束
        }else if(newDate.after(endDate)){
            secKillStatus = 2;
            remainSeconds = -1;
        }else{
            secKillStatus = 1;
            remainSeconds = 0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setTuser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
//        return "goodsDetail";

        return RespBean.success(detailVo);
    }
}
