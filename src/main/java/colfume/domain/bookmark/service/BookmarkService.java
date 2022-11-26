package colfume.domain.bookmark.service;

import colfume.domain.bookmark.model.Bookmark;
import colfume.domain.bookmark.model.repository.BookmarkRepository;
import colfume.domain.bookmark.service.exception.BookmarkAlreadyCreatedException;
import colfume.domain.bookmark.service.exception.BookmarkNotFoundException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.PerfumeRepository;
import colfume.domain.perfume.service.PerfumeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;

    /**
     * 향수에 대한 북마크가 이미 존재할 때 : 삭제된 상태면 create 상태로 변경, 아니면 예외 발생
     * 향수에 대한 북마크가 없을 때 : 북마크를 새로 생성 후 레포지토리에 저장
     */
    @Transactional
    public Long makeBookmark(Long userId, Long perfumeId, String redirectUrl) {
        Member member = memberRepository.getReferenceById(userId);
        Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow(PerfumeNotFoundException::new);
        final Long[] id = {null};

        bookmarkRepository.findByMemberAndPerfume(member, perfume)
                .ifPresentOrElse(bookmark -> {
                    if (bookmark.isDeleted()) {
                        bookmark.create();
                        id[0] = bookmark.getId();

                    } else throw new BookmarkAlreadyCreatedException();

                }, () -> {
                    Bookmark bookmark = new Bookmark(member, perfume, redirectUrl);
                    id[0] = bookmarkRepository.save(bookmark).getId();
                    perfume.addLikes();
                });

        return id[0];
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findWithPerfumeById(bookmarkId).orElseThrow(BookmarkNotFoundException::new);
        bookmark.delete(); // perfume.subtractLikes()
    }

    @Transactional(readOnly = true)
    public Long findPerfumeIdByBookmarkId(Long bookmarkId) {
        return bookmarkRepository.findPerfumeIdById(bookmarkId)
                .orElseThrow(PerfumeNotFoundException::new);
    }
}