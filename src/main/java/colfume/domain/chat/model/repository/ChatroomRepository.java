package colfume.domain.chat.model.repository;

import colfume.domain.chat.model.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
