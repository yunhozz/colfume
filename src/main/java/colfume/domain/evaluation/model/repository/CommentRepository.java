package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.evaluation e where c.id = :id")
    Optional<Comment> findWithEvaluationById(@Param("id") Long id);

    @Query("select c from Comment c join fetch c.writer w join fetch c.evaluation e where c.id = :id")
    Optional<Comment> findWithWriterAndEvaluationById(@Param("id") Long id);

    @Query("select c from Comment c join c.writer w where c.id = :id and w.id = :userId")
    Optional<Comment> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("select c from Comment c join fetch c.evaluation e where e.id = :evaluationId")
    List<Comment> findListByEvaluationId(@Param("evaluationId") Long evaluationId);
}