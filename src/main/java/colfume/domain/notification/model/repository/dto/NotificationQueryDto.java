package colfume.domain.notification.model.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationQueryDto {

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