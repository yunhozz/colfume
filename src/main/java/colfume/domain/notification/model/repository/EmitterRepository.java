package colfume.domain.notification.model.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {

    SseEmitter saveEmitter(String emitterId, SseEmitter emitter);
    void saveEventCache(String eventId, Object event);
    Map<String, SseEmitter> findEmittersWithUserId(String userId);
    Map<String, Object> findEventCachesWithUserId(String userId);
    void deleteById(String emitterId);
    void deleteByEventId(String eventId);
}
