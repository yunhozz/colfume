package colfume.dto;

import colfume.domain.chat.model.entity.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class ChatDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRequestDto {

        private Long chatroomId;

        @NotBlank(message = "메세지를 입력해주세요.")
        private String message;

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChatroomResponseDto {

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
}
