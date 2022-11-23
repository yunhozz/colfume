package colfume.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String message;
    private String redirectUrl;
    private Boolean isChecked;
    private LocalDateTime createdDate;
}