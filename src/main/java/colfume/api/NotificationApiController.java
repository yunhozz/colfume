package colfume.api;

import colfume.api.dto.Response;
import colfume.oauth.UserPrincipal;
import colfume.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static colfume.common.dto.NotificationDto.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping
    public Response getNotificationsWithReceiverId(@AuthenticationPrincipal UserPrincipal user) {
        return Response.success(notificationService.findNotificationDtoWithReceiverId(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response getNotification(@PathVariable String id) {
        notificationService.readNotification(Long.valueOf(id));
        return Response.success(notificationService.findNotificationDto(Long.valueOf(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/connect", produces = "text/event-stream")
    public Response connect(@RequestHeader(value = "Last-Event-Id", defaultValue = "") String lastEventId, @AuthenticationPrincipal UserPrincipal user) {
        notificationService.connect(user.getId(), lastEventId);
        return Response.success(HttpStatus.CREATED);
    }

    @PostMapping
    public Response createNotification(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody NotificationRequestDto notificationRequestDto, @RequestParam String receiverId) {
        return Response.success(notificationService.sendNotification(notificationRequestDto, user.getId(), Long.valueOf(receiverId)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Response deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(Long.valueOf(id));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}