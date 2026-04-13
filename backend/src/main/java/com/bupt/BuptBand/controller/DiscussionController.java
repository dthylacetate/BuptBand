package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.*;
import com.bupt.BuptBand.model.*;
import com.bupt.BuptBand.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController
{

    @Autowired
    private DiscussionService discussionService;
    @Autowired
    private AppUserService userService;
    @Autowired
    private LikeService likeService;

    //获取列表 (DTO 转换逻辑)
    @GetMapping
    public List<DiscussionPostDTO> getPosts(@RequestParam DiscussionSection section, Principal principal)
    {
        return discussionService.getPostsBySection(section).stream()
                .map(post -> convertToPostDTO(post, principal)).collect(Collectors.toList());
    }

    //获取帖子详情
    @GetMapping("/{id}")
    public DiscussionPostDTO getPost(@PathVariable Long id, Principal principal)
    {
        return convertToPostDTO(discussionService.getPostById(id), principal);
    }

    //发布帖子
    @PostMapping
    public DiscussionPostDTO createPost(@RequestBody DiscussionPost post, Principal principal)
    {
        AppUser user = userService.findByNickname(principal.getName());
        post.setPublisher(user);
        return convertToPostDTO(discussionService.createPost(post), principal);
    }

    //获取评论
    @GetMapping("/{postId}/comments")
    public List<CommentDTO> getComments(@PathVariable Long postId)
    {
        return discussionService.getCommentsByPost(postId).stream()
                .map(this::convertToCommentDTO).collect(Collectors.toList());
    }

    //发表评论
    @PostMapping("/{postId}/comments")
    public CommentDTO addComment(@PathVariable Long postId, @RequestBody Comment comment, Principal principal)
    {
        AppUser user = userService.findByNickname(principal.getName());
        comment.setUser(user);

        DiscussionPost post = new DiscussionPost();
        post.setId(postId);
        comment.setPost(post);

        return convertToCommentDTO(discussionService.addComment(comment));
    }

    // 转换工具方法
    private DiscussionPostDTO convertToPostDTO(DiscussionPost post, Principal principal) {
        DiscussionPostDTO dto = new DiscussionPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setSection(post.getSection());
        dto.setCreatedAt(post.getCreatedAt());
        if (post.getPublisher() != null)
        {
            dto.setPublisherNickname(post.getPublisher().getNickname());
            dto.setPublisherAvatar(post.getPublisher().getAvatarUrl());
        }

        // 添加点赞信息
        AppUser currentUser = null;
        if (principal != null) {
            currentUser = userService.findByNickname(principal.getName());
        }
        LikeStatusDTO likeStatus = likeService.getLikeStatus(currentUser, TargetType.DISCUSSION_POST, post.getId());
        dto.setLikeCount(likeStatus.getLikeCount());
        dto.setIsLiked(likeStatus.getIsLiked());

        return dto;
    }

    private CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        if (comment.getUser() != null) {
            dto.setUserNickname(comment.getUser().getNickname());
            dto.setUserAvatar(comment.getUser().getAvatarUrl());
        }
        return dto;
    }
}