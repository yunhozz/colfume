package colfume.common.dto;

import colfume.domain.notification.model.entity.Notification;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class NotificationDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationRequestDto {

        @NotBlank(message = "알림 내용을 입력해주세요.")
        private String message;

        private String redirectUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationResponseDto {

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

    @Getter
    @NoArgsConstructor
    public static class NotificationQueryDto {

        // notification
        private Long id;
        private String message;
        private String redirectUrl;
        private Boolean isChecked;
        private LocalDateTime createdDate;

        // member
        private Long senderId;
        private Long receiverId;
        private String name;

        @QueryProjection
        public NotificationQueryDto(Long id, String message, String redirectUrl, Boolean isChecked, LocalDateTime createdDate, Long senderId, Long receiverId, String name) {
            this.id = id;
            this.message = message;
            this.redirectUrl = redirectUrl;
            this.isChecked = isChecked;
            this.createdDate = createdDate;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.name = name;
        }
    }
}
