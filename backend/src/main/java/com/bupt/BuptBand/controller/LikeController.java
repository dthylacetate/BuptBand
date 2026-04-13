package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.LikeStatusDTO;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.TargetType;
import com.bupt.BuptBand.service.AppUserService;
import com.bupt.BuptBand.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private AppUserService userService;

    /**
     * 切换点赞状态
     * POST /api/likes/{targetType}/{targetId}
     * 示例: POST /api/likes/RECRUITMENT/1
     */
    @PostMapping("/{targetType}/{targetId}")
    public ResponseEntity<LikeStatusDTO> toggleLike(
            @PathVariable TargetType targetType,
            @PathVariable Long targetId,
            Principal principal) {

        // 获取当前登录用户
        AppUser user = userService.findByNickname(principal.getName());

        // 执行点赞/取消点赞
        LikeStatusDTO result = likeService.toggleLike(user, targetType, targetId);

        return ResponseEntity.ok(result);
    }

    /**
     * 获取点赞状态
     * GET /api/likes/{targetType}/{targetId}
     * 示例: GET /api/likes/RECRUITMENT/1
     */
    @GetMapping("/{targetType}/{targetId}")
    public ResponseEntity<LikeStatusDTO> getLikeStatus(
            @PathVariable TargetType targetType,
            @PathVariable Long targetId,
            Principal principal) {

        // 获取当前登录用户（可能为null，如果未登录）
        AppUser user = null;
        if (principal != null) {
            user = userService.findByNickname(principal.getName());
        }

        // 获取点赞状态
        LikeStatusDTO result = likeService.getLikeStatus(user, targetType, targetId);

        return ResponseEntity.ok(result);
    }
}
