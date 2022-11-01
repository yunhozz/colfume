package colfume.api.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDto {

    private Long chatroomId;

    @NotBlank(message = "메세지를 입력해주세요.")
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}