package colfume.domain.evaluation.controller;

import colfume.common.dto.Response;
import colfume.domain.evaluation.dto.request.CommentRequestDto;
import colfume.domain.evaluation.service.CommentService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public Response makeComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody CommentRequestDto commentRequestDto, @RequestParam String evaluationId) {
        return Response.success(commentService.createComment(commentRequestDto, userPrincipal.getId(), Long.valueOf(evaluationId)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public Response updateComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody CommentRequestDto commentRequestDto, @PathVariable String id) {
        commentService.updateContent(Long.valueOf(id), userPrincipal.getId(), commentRequestDto);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Response deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String id) {
        commentService.deleteComment(Long.valueOf(id), userPrincipal.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}