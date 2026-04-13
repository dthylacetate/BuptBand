package com.bupt.BuptBand.service;

import com.bupt.BuptBand.dto.LikeStatusDTO;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.LikeRecord;
import com.bupt.BuptBand.model.TargetType;
import com.bupt.BuptBand.repository.LikeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    @Autowired
    private LikeRecordRepository likeRecordRepository;

    /**
     * 切换点赞状态（如果已点赞则取消，未点赞则添加）
     */
    @Transactional
    public LikeStatusDTO toggleLike(AppUser user, TargetType targetType, Long targetId) {
        // 查找现有点赞记录
        LikeRecord existingRecord = likeRecordRepository
                .findByUserIdAndTargetTypeAndTargetId(user.getId(), targetType, targetId)
                .orElse(null);

        if (existingRecord != null) {
            // 已点赞，执行取消点赞
            likeRecordRepository.delete(existingRecord);
        } else {
            // 未点赞，执行点赞
            LikeRecord newRecord = new LikeRecord();
            newRecord.setUser(user);
            newRecord.setTargetType(targetType);
            newRecord.setTargetId(targetId);
            likeRecordRepository.save(newRecord);
        }

        // 返回更新后的点赞状态
        return getLikeStatus(user, targetType, targetId);
    }

    /**
     * 获取点赞状态（是否已点赞，点赞总数）
     */
    public LikeStatusDTO getLikeStatus(AppUser user, TargetType targetType, Long targetId) {
        LikeStatusDTO dto = new LikeStatusDTO();

        // 获取点赞总数
        long count = likeRecordRepository.countByTargetTypeAndTargetId(targetType, targetId);
        dto.setLikeCount((int) count);

        // 检查当前用户是否已点赞
        boolean isLiked = false;
        if (user != null) {
            isLiked = likeRecordRepository.existsByUserIdAndTargetTypeAndTargetId(
                    user.getId(), targetType, targetId);
        }
        dto.setIsLiked(isLiked);

        return dto;
    }
}
