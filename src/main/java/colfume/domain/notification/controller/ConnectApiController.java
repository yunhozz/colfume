package colfume.domain.notification.controller;

import colfume.common.dto.Response;
import colfume.domain.notification.service.ConnectService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/connect")
@RequiredArgsConstructor
public class ConnectApiController {

    private final ConnectService connectService;

    @PostMapping(produces = "text/event-stream")
    public Response connect(@RequestHeader(value = "Last-Event-Id", defaultValue = "") String lastEventId, @AuthenticationPrincipal UserPrincipal user) {
        connectService.connect(user.getId(), lastEventId);
        return Response.success(HttpStatus.CREATED);
    }
}