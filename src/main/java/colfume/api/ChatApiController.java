package colfume.api;

import colfume.domain.member.service.UserDetailsImpl;
import colfume.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static colfume.dto.ChatDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatApiController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/join")
    public void join(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody ChatRequestDto chatRequestDto) {
        chatRequestDto.setMessage(userDetails.getName() + "님이 입장하셨습니다.");
        template.convertAndSend("/subscribe/chat/room/" + chatRequestDto.getChatroomId(), chatRequestDto);
    }

    @MessageMapping("/chat/message")
    public void message(@Valid @RequestBody ChatRequestDto chatRequestDto) {
        template.convertAndSend("/subscribe/chat/room/" + chatRequestDto.getChatroomId(), chatRequestDto);
    }
}
