package colfume.common.converter.entity;

import colfume.api.dto.notification.NotificationRequestDto;
import colfume.domain.member.model.entity.Member;
import colfume.domain.notification.model.entity.Notification;
import colfume.domain.notification.service.dto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationConverter<S extends Member, R extends Member> implements EntityConverter<Notification, NotificationRequestDto, NotificationResponseDto> {

    private final S sender;
    private final R receiver;

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
}