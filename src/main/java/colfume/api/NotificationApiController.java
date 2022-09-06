package colfume.api;

import colfume.domain.member.service.UserDetailsImpl;
import colfume.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<NotificationResponseDto> getNotification(@PathVariable String notificationId) {
        notificationService.readNotification(Long.valueOf(notificationId));
        return ResponseEntity.ok(notificationService.findNotificationDto(Long.valueOf(notificationId)));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationQueryDto>> getNotificationsWithReceiverId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.findNotificationDtoWithReceiverId(userDetails.getId()));
    }

    @PostMapping(value = "/notifications/connect", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> connect(@RequestHeader(value = "Last-Event-Id", defaultValue = "") String lastEventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.connect(userDetails.getId(), lastEventId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/notifications")
    public ResponseEntity<Long> createNotification(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody NotificationRequestDto notificationRequestDto, @RequestParam String receiverId) {
        return new ResponseEntity<>(notificationService.sendNotification(notificationRequestDto, userDetails.getId(), Long.valueOf(receiverId)), HttpStatus.CREATED);
    }

    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteNotification(@RequestParam String notificationId) {
        notificationService.deleteNotification(Long.valueOf(notificationId));
        return ResponseEntity.noContent().build();
    }
}
