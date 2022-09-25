package colfume.api;

import colfume.domain.chat.service.ChatroomService;
import colfume.oauth.UserPrincipal;
import colfume.dto.ErrorResponseDto;
import colfume.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static colfume.dto.ChatDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatroomApiController {

    private final ChatroomService chatroomService;

    @GetMapping("/chat/rooms")
    public ResponseEntity<List<ChatroomResponseDto>> getChatroomList() {
        return ResponseEntity.ok(chatroomService.findChatroomDtoList());
    }

    @PostMapping("/chat/rooms")
    public ResponseEntity<Object> createChatroom(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String title) {
        if (!StringUtils.hasText(title)) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ErrorCode.CHATROOM_TITLE_NOT_INSERTED));
        }
        return new ResponseEntity<>(chatroomService.makeChatroom(user.getId(), title), HttpStatus.CREATED);
    }

    @PatchMapping("/chat/rooms")
    public ResponseEntity<Object> updateChatroomTitle(@AuthenticationPrincipal UserPrincipal user, @RequestParam String chatroomId, @RequestParam(required = false) String title) {
        if (!StringUtils.hasText(title)) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ErrorCode.CHATROOM_TITLE_NOT_INSERTED));
        }
        chatroomService.updateTitle(Long.valueOf(chatroomId), user.getId(), title);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/chat/rooms")
    public ResponseEntity<Void> deleteChatroom(@AuthenticationPrincipal UserPrincipal user, @RequestParam String chatroomId) {
        chatroomService.deleteChatroom(Long.valueOf(chatroomId), user.getId());
        return ResponseEntity.noContent().build();
    }
}
