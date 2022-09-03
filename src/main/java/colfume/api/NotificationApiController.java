package colfume.api;

import colfume.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.util.List;

import static colfume.dto.NotificationDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/{id}")
    public ResponseEntity<NotificationResponseDto> getNotification(@PathVariable String id) {
        return ResponseEntity.ok(notificationService.findNotificationDto(Long.valueOf(id)));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationQueryDto>> getNotificationsWithReceiverId(@RequestParam String receiverId) {
        return ResponseEntity.ok(notificationService.findNotificationDtoWithReceiverId(Long.valueOf(receiverId)));
    }

    @GetMapping("/notifications/connect")
    public ResponseEntity<SseEmitter> connect(@RequestHeader(value = "Last-Event-Id", defaultValue = "") String lastEventId, @RequestParam String userId) {
        return ResponseEntity.ok(notificationService.connect(Long.valueOf(userId), lastEventId));
    }

    @PostMapping("/notifications")
    public ResponseEntity<Long> createNotification(@Valid @RequestBody NotificationRequestDto notificationRequestDto, @RequestParam String senderId, @RequestParam String receiverId) {
        return ResponseEntity.ok(notificationService.sendNotification(notificationRequestDto, Long.valueOf(senderId), Long.valueOf(receiverId)));
    }

    @PatchMapping("/notifications")
    public ResponseEntity<Void> readNotification(@RequestParam String notificationId) {
        notificationService.readNotification(Long.valueOf(notificationId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteNotification(@RequestParam String notificationId) {
        notificationService.deleteNotification(Long.valueOf(notificationId));
        return ResponseEntity.noContent().build();
    }
}
