package com.example.mc_monitor.consumer;

import com.example.mc_monitor.entity.User;
import com.example.mc_monitor.model.ServerJoinEvent;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerJoinConsumer {

    private final String QUEUE_NAME = "mc.player.log";
    private final String MODULE_NAME = "[PlayerJoinConsumer] ";
    private final Gson gson = new Gson();

    private final CachingConnectionFactory connectionFactory;

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory",
            batch = "true",
            concurrency = "1"
    )
    public void receiveMessages(final Message message) {
        String str = convertBytesToString(message.getBody());
        log.info(MODULE_NAME + "Consume : {}", str);

        ServerJoinEvent serverJoinEvent = gson.fromJson(str, ServerJoinEvent.class);
        log.info(MODULE_NAME + "Parsing Data : {}", serverJoinEvent);
    }

    private String convertBytesToString(byte[] bytes) {
        try {
            // byte[] -> String 변환 (UTF-8)
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(MODULE_NAME + "Error converting byte[] to String", e);
            return "";  // 변환 실패 시 빈 문자열 반환 (혹은 적절한 에러 처리)
        }
    }
}
