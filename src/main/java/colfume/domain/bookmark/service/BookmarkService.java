package colfume.domain.bookmark.service;

import colfume.domain.bookmark.model.entity.Bookmark;
import colfume.domain.bookmark.model.repository.BookmarkRepository;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.enums.ErrorCode;
import colfume.exception.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;

    public Long makeBookmark(Long userId, Long perfumeId, String redirectUrl) {
        Member member = memberRepository.getReferenceById(userId);
        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException(ErrorCode.PERFUME_NOT_FOUND));
        Bookmark bookmark = new Bookmark(member, perfume, redirectUrl);

        return bookmarkRepository.save(bookmark).getId();
    }

    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = findBookmark(bookmarkId);
        bookmark.delete(); // soft delete
    }

    @Transactional(readOnly = true)
    private Bookmark findBookmark(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow();
    }
}