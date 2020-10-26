/*
 * Copyright 2018-2020 Pnoker. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.center.data.service.rabbit;

import com.dc3.center.data.service.PointValueService;
import com.dc3.common.bean.driver.DeviceEvent;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 接收驱动发送过来的数据
 *
 * @author pnoker
 */
@Slf4j
@Component
public class DeviceEventReceiver {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private PointValueService pointValueService;

    @RabbitHandler
    @RabbitListener(queues = "#{eventQueue.name}")
    public void pointValueReceive(Channel channel, Message message, DeviceEvent deviceEvent) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.debug("Device Event from {}", message.getMessageProperties().getReceivedRoutingKey());

            if (null == deviceEvent || null == deviceEvent.getDeviceId() || null == deviceEvent.getType()) {
                log.error("Invalid device event: {}", deviceEvent);
                return;
            }

            threadPoolExecutor.execute(() -> {
                // Insert device point data to MongoDB
                // TODO 可根据项目并发情况实现一个定时和批量入库逻辑
                pointValueService.addDeviceEvent(deviceEvent);
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}