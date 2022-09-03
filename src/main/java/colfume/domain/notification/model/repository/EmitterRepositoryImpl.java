package colfume.domain.notification.model.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    Map<String, Object> eventCaches = new ConcurrentHashMap<>();

    @Override
    public SseEmitter saveEmitter(String emitterId, SseEmitter emitter) {
        emitters.put(emitterId, emitter);
        return emitter;
    }

    @Override
    public void saveEventCache(String eventId, Object event) {
        eventCaches.put(eventId, event);
    }

    @Override
    public Map<String, SseEmitter> findEmittersWithUserId(String userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<String, Object> findEventCachesWithUserId(String userId) {
        return eventCaches.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteById(String emitterId) {
        emitters.keySet().forEach(key -> {
            if (key.equals(emitterId)) {
                emitters.remove(key);
            }
        });
    }

    @Override
    public void deleteByEventId(String eventId) {
        eventCaches.keySet().forEach(key -> {
            if (key.equals(eventId)) {
                emitters.remove(key);
            }
        });
    }
}
