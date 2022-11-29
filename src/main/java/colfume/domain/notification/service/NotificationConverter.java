package colfume.domain.notification.service;

import colfume.common.EntityConverter;
import colfume.domain.member.model.entity.Member;
import colfume.domain.notification.dto.NotificationRequestDto;
import colfume.domain.notification.dto.NotificationResponseDto;
import colfume.domain.notification.model.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationConverter implements EntityConverter<Notification, NotificationRequestDto, NotificationResponseDto> {

    private Member sender;
    private Member receiver;

    protected NotificationConverter(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public Notification convertToEntity(NotificationRequestDto notificationRequestDto) {
        if (sender == null || receiver == null) {
            throw new IllegalStateException("연관된 엔티티가 생성되지 않았습니다.");
        }

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