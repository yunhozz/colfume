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

    @Transactional
    public Long makeBookmark(Long userId, Long perfumeId, String redirectUrl) {
        Member member = memberRepository.getReferenceById(userId);
        Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow(PerfumeNotFoundException::new);

        if (bookmarkRepository.findByMemberAndPerfume(member, perfume).isPresent()) {
            throw new BookmarkAlreadyCreatedException();
        }

        Bookmark bookmark = new Bookmark(member, perfume, redirectUrl);
        perfume.addLikes();

        return bookmarkRepository.save(bookmark).getId();
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findWithPerfumeById(bookmarkId)
                .orElseThrow(BookmarkNotFoundException::new);

        bookmark.subtractPerfumeLikes(); // perfume.subtractLikes()
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public Long findPerfumeIdByBookmarkId(Long bookmarkId) {
        return bookmarkRepository.findPerfumeIdById(bookmarkId)
                .orElseThrow(PerfumeNotFoundException::new);
    }
}