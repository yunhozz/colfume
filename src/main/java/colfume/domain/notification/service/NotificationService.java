package colfume.domain.notification.service;

import colfume.api.dto.notification.NotificationRequestDto;
import colfume.common.converter.entity.NotificationConverter;
import colfume.common.enums.ErrorCode;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.exception.MemberNotFoundException;
import colfume.domain.notification.model.entity.Notification;
import colfume.domain.notification.model.repository.EmitterRepository;
import colfume.domain.notification.model.repository.NotificationRepository;
import colfume.domain.notification.service.dto.NotificationResponseDto;
import colfume.domain.notification.service.exception.NotificationNotFoundException;
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
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final MemberRepository memberRepository;
    private final NotificationConverter converter;

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

    @Transactional
    public Long sendNotification(NotificationRequestDto notificationRequestDto, Long senderId, Long receiverId) {
        Member sender = memberRepository.getReferenceById(senderId);
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new MemberNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        converter.update(sender, receiver);
        Notification notification = converter.convertToEntity(notificationRequestDto);

        Map<String, SseEmitter> emitters = emitterRepository.findEmittersWithUserId(String.valueOf(receiverId));
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, String.valueOf(receiverId), converter.convertToDto(notification));
                }
        );

        return notificationRepository.save(notification).getId();
    }

    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = findNotification(notificationId);
        notification.check();
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = findNotification(notificationId);
        notificationRepository.delete(notification);
    }

    @Transactional(readOnly = true)
    public NotificationResponseDto findNotificationDto(Long notificationId) {
        Notification notification = findNotification(notificationId);
        return converter.convertToDto(notification);
    }

    private Notification findNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND));
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
