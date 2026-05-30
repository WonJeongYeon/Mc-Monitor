package com.example.mc_monitor.consumer;

import com.example.mc_monitor.model.BaseEvent;
import com.example.mc_monitor.model.enums.EventType;
import com.example.mc_monitor.module.PlayerManager;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayerJoinConsumer {

    private final String QUEUE_NAME = "mc.player.log";
    private final String MODULE_NAME = "[PlayerJoinConsumer] ";
    private final Gson gson = new Gson();

    private final PlayerManager playerManager;
    private final CachingConnectionFactory connectionFactory;

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "rabbitListenerContainerFactory",
        batch = "true",
        concurrency = "1"
    )
    public void receiveMessages(final Message message) {
        String body = convertBytesToString(message.getBody());
        log.info(MODULE_NAME + "Consume : {}", body);

        BaseEvent event = BaseEvent.of(EventType.PLAYER_JOIN_EVENT, body);

        playerManager.processEvent(event).subscribe();
        log.info(MODULE_NAME + "Try to save data : {}", event);
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
