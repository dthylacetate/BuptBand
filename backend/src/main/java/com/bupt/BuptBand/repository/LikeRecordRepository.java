package com.bupt.BuptBand.repository;

import com.bupt.BuptBand.model.LikeRecord;
import com.bupt.BuptBand.model.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRecordRepository extends JpaRepository<LikeRecord, Long> {

    // 查找用户对某个目标的点赞记录
    Optional<LikeRecord> findByUserIdAndTargetTypeAndTargetId(
            Long userId, TargetType targetType, Long targetId);

    // 统计某个目标的点赞数
    long countByTargetTypeAndTargetId(TargetType targetType, Long targetId);

    // 检查用户是否已点赞某个目标
    boolean existsByUserIdAndTargetTypeAndTargetId(
            Long userId, TargetType targetType, Long targetId);

    // 获取某个目标的所有点赞记录
    List<LikeRecord> findByTargetTypeAndTargetId(TargetType targetType, Long targetId);

    // 删除用户对某个目标的点赞
    void deleteByUserIdAndTargetTypeAndTargetId(
            Long userId, TargetType targetType, Long targetId);
}
