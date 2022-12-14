package colfume.domain.chat.controller;

import colfume.common.dto.Response;
import colfume.domain.chat.service.ChatroomService;
import colfume.oauth.model.UserPrincipal;
import colfume.common.dto.ErrorResponseDto;
import colfume.common.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatroomApiController {

    private final ChatroomService chatroomService;

    @GetMapping
    public Response getChatroomList() {
        return Response.success(chatroomService.findChatroomDtoList(), HttpStatus.OK);
    }

    @PostMapping
    public Response createChatroom(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String title) {
        if (!StringUtils.hasText(title)) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.CHATROOM_TITLE_NOT_INSERTED);
            return Response.failure(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
        }

        return Response.success(chatroomService.makeChatroom(user.getId(), title), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public Response updateChatroomTitle(@AuthenticationPrincipal UserPrincipal user, @PathVariable String id, @RequestParam(required = false) String title) {
        if (!StringUtils.hasText(title)) {
            ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.CHATROOM_TITLE_NOT_INSERTED);
            return Response.failure(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
        }

        chatroomService.updateTitle(Long.valueOf(id), user.getId(), title);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Response deleteChatroom(@AuthenticationPrincipal UserPrincipal user, @RequestParam String id) {
        chatroomService.deleteChatroom(Long.valueOf(id), user.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}