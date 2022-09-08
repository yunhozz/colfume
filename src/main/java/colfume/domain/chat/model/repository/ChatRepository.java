package colfume.domain.chat.model.repository;

import colfume.domain.chat.model.entity.Chat;
import colfume.domain.chat.model.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByChatroom(Chatroom chatroom);
}
