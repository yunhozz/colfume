package colfume.domain.notification.model.repository;

import colfume.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

    @Query("select n from Notification n join fetch n.sender s join fetch n.receiver r where n.id = :id")
    Optional<Notification> findWithSenderAndReceiverById(@Param("id") Long id);
}