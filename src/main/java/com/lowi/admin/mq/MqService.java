package com.lowi.admin.mq;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.lowi.admin.utils.Result;
import com.lowi.admin.utils.RunTimeUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * MqController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/10 10:44
 */
@RestController
@RequestMapping("mq")
public class MqService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${mq.producerGroup}")
    private String producerGroup;
    @Value("${mq.serverAddr}")
    private String serverAddr;

    @RequestMapping("/send")
    public Result send(String topic) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("data", "Hello World");
        String toJsonStr = JSONUtil.toJsonStr(map);
        Result send = send(topic, toJsonStr,0);
        if (send.getCode() == 0) {
            Result delay = send("delay",toJsonStr,5);
            return delay;
        }
        return send;
    }

    @RequestMapping("/pay")
    public Result delay(String topic) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("data", "Hello World");
        String toJsonStr = JSONUtil.toJsonStr(map);
        return send(topic, toJsonStr,0);
    }


    public Result send(String topic, String toJsonStr,Integer delayTime) {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        try {
            producer.setVipChannelEnabled(false);
            producer.setInstanceName(RunTimeUtil.getRocketMqUniqeInstanceName());
            producer.setNamesrvAddr(serverAddr);
            producer.start();
            Message message = new Message(topic, "*", toJsonStr.getBytes(RemotingHelper.DEFAULT_CHARSET));
            message.setDelayTimeLevel(delayTime);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            SendResult sendResult = producer.send(message);
            logger.info("发送响应：" + sendResult.getMsgId() + "，发送状态：" + sendResult.getSendStatus());
            logger.info(sendResult.toString());
            stopWatch.stop();
            logger.info("发送消息耗时：" + stopWatch.getTotalTimeMillis());
            return Result.getInstance(0, "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("生产者服务发送消息到Producer失败", e);
            return Result.getInstance(1, "失败");
        } finally {
            producer.shutdown();
        }
    }
}
