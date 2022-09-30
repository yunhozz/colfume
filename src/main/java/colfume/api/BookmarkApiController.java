package colfume.api;

import colfume.api.dto.Response;
import colfume.domain.bookmark.model.repository.BookmarkRepository;
import colfume.domain.bookmark.service.BookmarkService;
import colfume.oauth.UserPrincipal;
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

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;
    private final BookmarkRepository bookmarkRepository;

    @GetMapping("/bookmark/{bookmarkId}")
    public Response getPerfumeIdForRedirectUrl(@PathVariable String bookmarkId) {
        return Response.success(bookmarkRepository.findPerfumeIdByBookmarkId(Long.valueOf(bookmarkId)), HttpStatus.OK);
    }

    @GetMapping("/bookmarks")
    public Response getBookmarkListByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Response.success(bookmarkRepository.findBookmarkListByUserId(userPrincipal.getId()), HttpStatus.OK);
    }

    @PostMapping("/bookmark")
    public Response makeBookmark(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String perfumeId, @NotBlank @RequestParam String redirectUrl) {
        return Response.success(bookmarkService.makeBookmark(userPrincipal.getId(), Long.valueOf(perfumeId), redirectUrl), HttpStatus.CREATED);
    }

    @DeleteMapping("/bookmark")
    public Response deleteBookmark(@RequestParam String bookmarkId) {
        bookmarkService.deleteBookmark(Long.valueOf(bookmarkId));
        return Response.success(HttpStatus.NO_CONTENT);
    }
}