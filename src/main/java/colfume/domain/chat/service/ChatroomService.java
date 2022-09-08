package colfume.domain.chat.service;

import colfume.domain.chat.model.entity.Chat;
import colfume.domain.chat.model.entity.Chatroom;
import colfume.domain.chat.model.repository.ChatRepository;
import colfume.domain.chat.model.repository.ChatroomRepository;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.enums.ErrorCode;
import colfume.exception.ChatroomNotFoundException;
import colfume.exception.ChatroomPermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static colfume.dto.ChatDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public Long makeChatroom(Long userId, String title) {
        Member member = memberRepository.getReferenceById(userId);
        Chatroom chatroom = new Chatroom(member, title);

        return chatroomRepository.save(chatroom).getId();
    }

    public void updateTitle(Long chatroomId, Long userId, String title) {
        Chatroom chatroom = findChatroom(chatroomId);
        if (!chatroom.getMember().getId().equals(userId)) {
            throw new ChatroomPermissionException(ErrorCode.CHATROOM_NOT_PERMISSION);
        }
        chatroom.updateTitle(title);
    }

    public void deleteChatroom(Long chatroomId, Long userId) {
        Chatroom chatroom = findChatroom(chatroomId);
        if (!chatroom.getMember().getId().equals(userId)) {
            throw new ChatroomPermissionException(ErrorCode.CHATROOM_NOT_PERMISSION);
        }
        List<Chat> chats = chatRepository.findByChatroom(chatroom);
        chats.forEach(chatRepository::delete);
        chatroomRepository.delete(chatroom);
    }

    @Transactional(readOnly = true)
    public ChatroomResponseDto findChatroomDto(Long chatroomId) {
        return new ChatroomResponseDto(findChatroom(chatroomId));
    }

    @Transactional(readOnly = true)
    public List<ChatroomResponseDto> findChatroomDtoList() {
        return chatroomRepository.findAll().stream()
                .map(ChatroomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private Chatroom findChatroom(Long chatroomId) {
        return chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new ChatroomNotFoundException(ErrorCode.CHATROOM_NOT_FOUND));
    }
}
