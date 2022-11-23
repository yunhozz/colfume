package colfume.domain.chat.dto;

import colfume.domain.chat.model.entity.Chatroom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatroomResponseDto {

    private Long id;
    private Long userId;
    private String title;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public ChatroomResponseDto(Chatroom chatroom) {
        id = chatroom.getId();
        userId = chatroom.getMember().getId();
        title = chatroom.getTitle();
        createdDate = chatroom.getCreatedDate();
        lastModifiedDate = chatroom.getLastModifiedDate();
    }
}