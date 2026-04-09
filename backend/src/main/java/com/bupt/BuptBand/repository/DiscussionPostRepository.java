package com.bupt.BuptBand.repository;

import com.bupt.BuptBand.model.DiscussionPost;
import com.bupt.BuptBand.model.DiscussionSection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscussionPostRepository extends JpaRepository<DiscussionPost, Long>
{
    // 核心需求：按板块查询并按时间倒序
    List<DiscussionPost> findBySectionOrderByCreatedAtDesc(DiscussionSection section);
}