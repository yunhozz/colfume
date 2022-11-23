package colfume.domain.notification.service;

import colfume.common.EntityConverter;
import colfume.domain.member.model.entity.Member;
import colfume.domain.notification.dto.NotificationRequestDto;
import colfume.domain.notification.dto.NotificationResponseDto;
import colfume.domain.notification.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter implements EntityConverter<Notification, NotificationRequestDto, NotificationResponseDto> {

    private Member sender;
    private Member receiver;

    @Override
    public Notification convertToEntity(NotificationRequestDto notificationRequestDto) {
        return Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .message(notificationRequestDto.getMessage())
                .redirectUrl(notificationRequestDto.getRedirectUrl())
                .build();
    }

    @Override
    public NotificationResponseDto convertToDto(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getSender().getId(),
                notification.getReceiver().getId(),
                notification.getMessage(),
                notification.getRedirectUrl(),
                notification.isChecked(),
                notification.getCreatedDate()
        );
    }

    public void setEntities(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }
}