package colfume.api;

import colfume.api.dto.Response;
import colfume.oauth.UserPrincipal;
import colfume.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static colfume.dto.NotificationDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/notifications/{notificationId}")
    public Response getNotification(@PathVariable String notificationId) {
        notificationService.readNotification(Long.valueOf(notificationId));
        return Response.success(notificationService.findNotificationDto(Long.valueOf(notificationId)), HttpStatus.OK);
    }

    @GetMapping("/notifications")
    public Response getNotificationsWithReceiverId(@AuthenticationPrincipal UserPrincipal user) {
        return Response.success(notificationService.findNotificationDtoWithReceiverId(user.getId()), HttpStatus.OK);
    }

    @PostMapping(value = "/notifications/connect", produces = "text/event-stream")
    public Response connect(@RequestHeader(value = "Last-Event-Id", defaultValue = "") String lastEventId, @AuthenticationPrincipal UserPrincipal user) {
        notificationService.connect(user.getId(), lastEventId);
        return Response.success(HttpStatus.CREATED);
    }

    @PostMapping("/notifications")
    public Response createNotification(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody NotificationRequestDto notificationRequestDto, @RequestParam String receiverId) {
        return Response.success(notificationService.sendNotification(notificationRequestDto, user.getId(), Long.valueOf(receiverId)), HttpStatus.CREATED);
    }

    @DeleteMapping("/notifications")
    public Response deleteNotification(@RequestParam String notificationId) {
        notificationService.deleteNotification(Long.valueOf(notificationId));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}