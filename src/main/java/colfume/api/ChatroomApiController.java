package colfume.api;

import colfume.api.dto.Response;
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
    public Response getChatroomList() {
        return Response.success(chatroomService.findChatroomDtoList(), HttpStatus.OK);
    }

    @PostMapping("/chat/rooms")
    public Response createChatroom(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String title) {
        if (!StringUtils.hasText(title)) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.CHATROOM_TITLE_NOT_INSERTED);
            return Response.failure(-1000, error.getMessage(), error, HttpStatus.BAD_REQUEST);
        }
        return Response.success(chatroomService.makeChatroom(user.getId(), title), HttpStatus.CREATED);
    }

    @PatchMapping("/chat/rooms")
    public Response updateChatroomTitle(@AuthenticationPrincipal UserPrincipal user, @RequestParam String chatroomId, @RequestParam(required = false) String title) {
        if (!StringUtils.hasText(title)) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.CHATROOM_TITLE_NOT_INSERTED);
            return Response.failure(-1000, error.getMessage(), error, HttpStatus.BAD_REQUEST);
        }
        chatroomService.updateTitle(Long.valueOf(chatroomId), user.getId(), title);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/chat/rooms")
    public Response deleteChatroom(@AuthenticationPrincipal UserPrincipal user, @RequestParam String chatroomId) {
        chatroomService.deleteChatroom(Long.valueOf(chatroomId), user.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}