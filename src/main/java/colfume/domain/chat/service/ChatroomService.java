package colfume.domain.chat.service;

import colfume.domain.chat.dto.ChatroomResponseDto;
import colfume.domain.chat.model.entity.Chat;
import colfume.domain.chat.model.entity.Chatroom;
import colfume.domain.chat.model.repository.ChatRepository;
import colfume.domain.chat.model.repository.ChatroomRepository;
import colfume.domain.chat.service.exception.ChatroomNotFoundException;
import colfume.domain.chat.service.exception.ChatroomPermissionException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long makeChatroom(Long userId, String title) {
        Member member = memberRepository.getReferenceById(userId);
        Chatroom chatroom = new Chatroom(member, title);

        return chatroomRepository.save(chatroom).getId();
    }

    @Transactional
    public void updateTitle(Long chatroomId, Long userId, String title) {
        Chatroom chatroom = findChatroom(chatroomId);

        if (!chatroom.getMember().getId().equals(userId)) {
            throw new ChatroomPermissionException();
        }

        chatroom.updateTitle(title);
    }

    @Transactional
    public void deleteChatroom(Long chatroomId, Long userId) {
        Chatroom chatroom = findChatroom(chatroomId);

        if (!chatroom.getMember().getId().equals(userId)) {
            throw new ChatroomPermissionException();
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

    private Chatroom findChatroom(Long chatroomId) {
        return chatroomRepository.findById(chatroomId)
                .orElseThrow(ChatroomNotFoundException::new);
    }
}
