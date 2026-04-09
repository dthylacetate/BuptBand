package com.bupt.BuptBand.service;

import com.bupt.BuptBand.model.ShowAnnouncement;
import com.bupt.BuptBand.repository.ShowAnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShowAnnouncementService
{
    @Autowired
    private ShowAnnouncementRepository showRepository;

    public ShowAnnouncement createShow(ShowAnnouncement show)
    {
        return showRepository.save(show);
    }

    public List<ShowAnnouncement> getAllShows()
    {
        // 获取所有演出，并按照演出时间先后排序
        return showRepository.findAllByOrderByShowTimeAsc();
    }

    public ShowAnnouncement getShowById(Long id)
    {
        return showRepository.findById(id).orElseThrow(() -> new RuntimeException("该演出公告不存在"));
    }

    // 如果后续想支持删除功能，可以在这加 delete 方法
}