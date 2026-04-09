package com.bupt.BuptBand.service;

import com.bupt.BuptBand.model.*;
import com.bupt.BuptBand.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscussionService
{
    @Autowired
    private DiscussionPostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    public DiscussionPost createPost(DiscussionPost post)
    {
        return postRepository.save(post);
    }

    public List<DiscussionPost> getPostsBySection(DiscussionSection section)
    {
        return postRepository.findBySectionOrderByCreatedAtDesc(section);
    }

    public DiscussionPost getPostById(Long id)
    {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("帖子不存在"));
    }

    public Comment addComment(Comment comment)
    {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId)
    {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
}