package colfume.domain.bookmark.service;

import colfume.common.enums.ErrorCode;
import colfume.domain.bookmark.model.entity.Bookmark;
import colfume.domain.bookmark.model.repository.BookmarkRepository;
import colfume.domain.bookmark.service.exception.BookmarkAlreadyCreatedException;
import colfume.domain.bookmark.service.exception.BookmarkNotFoundException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.exception.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;

    @Transactional
    public Long makeBookmark(Long userId, Long perfumeId, String redirectUrl) {
        Member member = memberRepository.getReferenceById(userId);
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));

        Bookmark bookmark = validateAndSaveBookmark(redirectUrl, member, perfume);
        perfume.addLikes(); // 좋아요 수 +1

        return bookmark.getId();
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = findBookmark(bookmarkId);
        bookmark.delete(); // soft delete
        bookmark.getPerfume().subtractLikes(); // 좋아요 수 -1
    }

    @Transactional(readOnly = true)
    public Long findPerfumeIdByBookmarkId(Long bookmarkId) {
        return bookmarkRepository.findPerfumeIdByBookmarkId(bookmarkId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));
    }

    /**
     * 향수에 대한 북마크가 이미 존재할 때 : 삭제된 상태면 create 상태로 변경, 아니면 예외 발생
     * 향수에 대한 북마크가 없을 때 : 북마크를 새로 생성 후 레포지토리에 저장
     */
    private Bookmark validateAndSaveBookmark(String redirectUrl, Member member, Perfume perfume) {
        Optional<Bookmark> optionalBookmark = bookmarkRepository.findByMemberAndPerfume(member, perfume);
        Bookmark bookmark;

        if (optionalBookmark.isPresent()) {
            bookmark = optionalBookmark.get();

            if (bookmark.isDeleted()) {
                bookmark.create();

            } else throw new BookmarkAlreadyCreatedException(ErrorCode.BOOKMARK_ALREADY_CREATED);

        } else {
            bookmark = new Bookmark(member, perfume, redirectUrl);
            bookmarkRepository.save(bookmark);
        }
        return bookmark;
    }

    private Bookmark findBookmark(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BookmarkNotFoundException(ErrorCode.BOOKMARK_NOT_FOUND));
    }
}