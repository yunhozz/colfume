package colfume.domain.notification.controller;

import colfume.common.dto.Response;
import colfume.domain.notification.dto.request.NotificationRequestDto;
import colfume.domain.notification.model.repository.NotificationRepository;
import colfume.domain.notification.service.NotificationService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @GetMapping
    public Response getNotificationsWithReceiverId(@AuthenticationPrincipal UserPrincipal user) {
        return Response.success(notificationRepository.findWithReceiverId(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Response getNotification(@PathVariable String id) {
        notificationService.readNotification(Long.valueOf(id));
        return Response.success(notificationService.findNotificationDto(Long.valueOf(id)), HttpStatus.OK);
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