package colfume.domain.notification.service;

import colfume.common.enums.ErrorCode;
import colfume.domain.notification.model.repository.EmitterRepository;
import colfume.domain.notification.service.exception.NotificationSendFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectService {

    private final EmitterRepository emitterRepository;

    private final static Long DEFAULT_TIMEOUT = 60 * 60 * 1000L; // 1 hour

    @Transactional
    public SseEmitter connect(Long userId, String lastEventId) {
        String emitterId = String.valueOf(userId) + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.saveEmitter(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> complete(emitterId));
        emitter.onTimeout(() -> complete(emitterId));
        sendToClient(emitter, emitterId, "EventStream Created. [user id = " + userId + "]");

        if (lastEventId != null) {
            Map<String, Object> events = emitterRepository.findEventCachesWithUserId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> entry.getKey().compareTo(lastEventId) > 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    private void complete(String emitterId) {
        log.info("emitter completed");
        emitterRepository.deleteById(emitterId);
    }

    private void sendToClient(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(emitterId)
                            .name("sse")
                            .data(data)
                            .build()
            );

        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            throw new NotificationSendFailException(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }
}