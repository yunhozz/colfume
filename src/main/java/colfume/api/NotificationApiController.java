package colfume.api;

import colfume.domain.member.service.UserDetailsImpl;
import colfume.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        notificationService.readNotification(Long.valueOf(id));
        return ResponseEntity.ok(notificationService.findNotificationDto(Long.valueOf(id)));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationQueryDto>> getNotificationsWithReceiverId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.findNotificationDtoWithReceiverId(userDetails.getId()));
    }

    @PostMapping(value = "/notifications/connect", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> connect(@RequestHeader(value = "Last-Event-Id", defaultValue = "") String lastEventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.connect(userDetails.getId(), lastEventId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications")
    public ResponseEntity<Long> createNotification(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody NotificationRequestDto notificationRequestDto, @RequestParam String receiverId) {
        return ResponseEntity.ok(notificationService.sendNotification(notificationRequestDto, userDetails.getId(), Long.valueOf(receiverId)));
    }

    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteNotification(@RequestParam String notificationId) {
        notificationService.deleteNotification(Long.valueOf(notificationId));
        return ResponseEntity.noContent().build();
    }
}
