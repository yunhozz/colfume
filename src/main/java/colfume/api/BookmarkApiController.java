package colfume.api;

import colfume.api.dto.Response;
import colfume.domain.bookmark.model.repository.BookmarkRepository;
import colfume.domain.bookmark.service.BookmarkService;
import colfume.common.oauth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping("/{id}")
    public Response getPerfumeIdForRedirectUrl(@PathVariable String id) {
        return Response.success(bookmarkRepository.findPerfumeIdByBookmarkId(Long.valueOf(id)), HttpStatus.OK);
    }

    @GetMapping
    public Response getBookmarkListByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Response.success(bookmarkRepository.findBookmarkListByUserId(userPrincipal.getId()), HttpStatus.OK);
    }

    @PostMapping
    public Response makeBookmark(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String perfumeId) {
        String redirectUrl = "http://localhost:8080/perfume/" + perfumeId;
        return Response.success(bookmarkService.makeBookmark(userPrincipal.getId(), Long.valueOf(perfumeId), redirectUrl), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public Response deleteBookmark(@PathVariable String id) {
        bookmarkService.deleteBookmark(Long.valueOf(id));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}