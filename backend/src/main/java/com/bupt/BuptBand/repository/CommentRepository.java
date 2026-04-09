package com.bupt.BuptBand.repository;

import com.bupt.BuptBand.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}