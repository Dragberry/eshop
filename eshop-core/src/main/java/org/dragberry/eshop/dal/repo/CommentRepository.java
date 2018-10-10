package org.dragberry.eshop.dal.repo;

import java.time.LocalDateTime;
import org.dragberry.eshop.dal.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, ProductCommentReposiroty {

    @Query("select max(c.createdDate) from Comment c where c.userIP = :ip")
    LocalDateTime findLastUserComment(String ip);

}
