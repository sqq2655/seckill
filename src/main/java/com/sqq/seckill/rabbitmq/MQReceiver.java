package com.sqq.seckill.rabbitmq;

import com.sqq.seckill.pojo.SeckillOrder;
import com.sqq.seckill.pojo.User;
import com.sqq.seckill.service.IGoodsService;
import com.sqq.seckill.service.IOrderService;
import com.sqq.seckill.utils.JsonUtil;
import com.sqq.seckill.vo.GoodsVo;
import com.sqq.seckill.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author sqq
 * @version 1.0
 * @date 2023/3/5 16:08
 */
@Service
@Slf4j
public class MQReceiver {


    @Autowired
    private IGoodsService iGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService iOrderService;


    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接收消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        //判断库存
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购
        SeckillOrder tSeckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (tSeckillOrder != null) {
            return;
        }
        //下单操作
        iOrderService.seckill(user, goodsVo);

    }



//    @RabbitListener(queues ="queue")
//    public void receive(Object msg){
//        log.info("接收消息:"+msg);
//    }
//
//    //fanout接收消息
//    @RabbitListener(queues ="queue_fanout01")
//    public void receive01(Object msg){
//        log.info("QUEUE01接收消息:"+msg);
//    }
//
//    @RabbitListener(queues ="queue_fanout02")
//    public void receive02(Object msg){
//        log.info("QUEUE02接收消息:"+msg);
//    }
//
//
//    @RabbitListener(queues ="queue_direct01")
//    public void receive03(Object msg){
//        log.info("QUEUE01接收消息:"+msg);
//    }
//
//    @RabbitListener(queues ="queue_direct02")
//    public void receive04(Object msg){
//        log.info("QUEUE02接收消息:"+msg);
//    }
//
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg) {
//        log.info("QUEUE01接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg) {
//        log.info("QUEUE02接收消息" + msg);
//    }
//
//    @RabbitListener(queues = "queue_header01")
//    public void receive07(Message message) {
//        log.info("QUEUE01接收消息 message对象" + message);
//        log.info("QUEUE01接收消息" + new String(message.getBody()));
//    }
//
//    @RabbitListener(queues = "queue_header02")
//    public void receive08(Message message) {
//        log.info("QUEUE02接收消息 message对象" + message);
//        log.info("QUEUE02接收消息" + new String(message.getBody()));
//    }

}
