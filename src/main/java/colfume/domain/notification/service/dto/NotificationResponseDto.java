package colfume.domain.notification.service.dto;

import colfume.domain.notification.model.entity.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private String redirectUrl;
    private Boolean isChecked;
    private LocalDateTime createdDate;

    public NotificationResponseDto(Notification notification) {
        id = notification.getId();
        senderId = notification.getSender().getId();
        receiverId = notification.getReceiver().getId();
        message = notification.getMessage();
        redirectUrl = notification.getRedirectUrl();
        isChecked = notification.isChecked();
        createdDate = notification.getCreatedDate();
    }
}