package colfume.domain.notification.service;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.exception.MemberNotFoundException;
import colfume.domain.notification.dto.NotificationRequestDto;
import colfume.domain.notification.dto.NotificationResponseDto;
import colfume.domain.notification.model.Notification;
import colfume.domain.notification.model.repository.EmitterRepository;
import colfume.domain.notification.model.repository.NotificationRepository;
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

    @Transactional
    public Long sendNotification(NotificationRequestDto notificationRequestDto, Long senderId, Long receiverId) {
        Member sender = memberRepository.getReferenceById(senderId);
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(MemberNotFoundException::new);

        NotificationConverter converter = new NotificationConverter(sender, receiver);
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
        Notification notification = notificationRepository.findWithSenderAndReceiverById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        NotificationConverter converter = new NotificationConverter();

        return converter.convertToDto(notification);
    }

    private Notification findNotification(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
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
            throw new NotificationSendFailException();
        }
    }
}