package colfume.domain.notification.model.repository;

import java.util.List;

import static colfume.common.dto.NotificationDto.*;

public interface NotificationRepositoryCustom {

    List<NotificationQueryDto> findWithReceiverId(Long receiverId);
}
