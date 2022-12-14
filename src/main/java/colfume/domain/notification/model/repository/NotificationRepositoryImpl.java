package colfume.domain.notification.model.repository;

import colfume.domain.member.model.entity.QMember;
import colfume.domain.notification.dto.NotificationQueryDto;
import colfume.domain.notification.dto.QNotificationQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colfume.domain.notification.model.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NotificationQueryDto> findWithReceiverId(Long receiverId) {
        QMember sender = new QMember("sender");
        QMember receiver = new QMember("receiver");

        return queryFactory
                .select(new QNotificationQueryDto(
                        notification.id,
                        notification.message,
                        notification.redirectUrl,
                        notification.isChecked,
                        notification.createdDate,
                        sender.id,
                        receiver.id,
                        sender.name
                ))
                .from(notification)
                .join(notification.sender, sender)
                .join(notification.receiver, receiver)
                .where(receiver.id.eq(receiverId))
                .orderBy(notification.createdDate.desc())
                .fetch();
    }
}
