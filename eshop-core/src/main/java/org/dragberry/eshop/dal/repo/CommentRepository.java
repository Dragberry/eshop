package org.dragberry.eshop.dal.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.dragberry.eshop.dal.entity.Comment;
import org.dragberry.eshop.dal.entity.ProductComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select max(c.createdDate) from Comment c where c.userIP = :ip")
    LocalDateTime findLastUserComment(String ip);

    @Query("select pc from ProductComment pc join pc.comment c order by c.dateTime desc")
    List<ProductComment> findProductComments(Pageable pageRequest);
    
}
