package com.bupt.BuptBand.repository;

import com.bupt.BuptBand.model.ShowAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ShowAnnouncementRepository extends JpaRepository<ShowAnnouncement, Long>
{
    // 核心逻辑：按演出时间升序排列（最近的演出排在最前面）
    List<ShowAnnouncement> findAllByOrderByShowTimeAsc();
}