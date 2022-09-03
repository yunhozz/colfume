package colfume.domain.notification.model.repository;

import colfume.dto.NotificationDto;

import java.util.List;

import static colfume.dto.NotificationDto.*;

public interface NotificationRepositoryCustom {

    List<NotificationQueryDto> findWithReceiverId(Long receiverId);
}
