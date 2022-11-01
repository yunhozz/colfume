package colfume.domain.notification.model.repository;

import colfume.domain.notification.model.repository.dto.NotificationQueryDto;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<NotificationQueryDto> findWithReceiverId(Long receiverId);
}
