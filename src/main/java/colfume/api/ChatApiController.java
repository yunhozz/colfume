package colfume.api;

import colfume.api.dto.chat.ChatRequestDto;
import colfume.oauth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatApiController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/join")
    public void join(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody ChatRequestDto chatRequestDto) {
        chatRequestDto.setMessage(user.getName() + "님이 입장하셨습니다.");
        template.convertAndSend("/subscribe/chat/room/" + chatRequestDto.getChatroomId(), chatRequestDto);
    }

    @MessageMapping("/message")
    public void message(@Valid @RequestBody ChatRequestDto chatRequestDto) {
        template.convertAndSend("/subscribe/chat/room/" + chatRequestDto.getChatroomId(), chatRequestDto);
    }
}