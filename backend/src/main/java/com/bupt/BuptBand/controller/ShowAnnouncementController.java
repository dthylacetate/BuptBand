package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.LikeStatusDTO;
import com.bupt.BuptBand.dto.ShowAnnouncementDTO;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.ShowAnnouncement;
import com.bupt.BuptBand.model.TargetType;
import com.bupt.BuptBand.service.AppUserService;
import com.bupt.BuptBand.service.LikeService;
import com.bupt.BuptBand.service.ShowAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shows")
public class ShowAnnouncementController
{

    @Autowired
    private ShowAnnouncementService showService;
    @Autowired
    private AppUserService userService;
    @Autowired
    private LikeService likeService;

    // 1. 获取所有演出列表
    @GetMapping
    public List<ShowAnnouncementDTO> getShows(Principal principal)
    {
        return showService.getAllShows().stream()
                .map(show -> convertToDTO(show, principal))
                .collect(Collectors.toList());
    }

    //获取演出详情
    @GetMapping("/{id}")
    public ShowAnnouncementDTO getShow(@PathVariable Long id, Principal principal)
    {
        return convertToDTO(showService.getShowById(id), principal);
    }

    //发布演出公告 (需登录)
    @PostMapping
    public ShowAnnouncementDTO createShow(@RequestBody ShowAnnouncement show, Principal principal)
    {
        AppUser user = userService.findByNickname(principal.getName());
        show.setPublisher(user);
        return convertToDTO(showService.createShow(show), principal);
    }

    //
    private ShowAnnouncementDTO convertToDTO(ShowAnnouncement show, Principal principal)
    {
        ShowAnnouncementDTO dto = new ShowAnnouncementDTO();
        dto.setId(show.getId());
        dto.setTitle(show.getTitle());
        dto.setVenue(show.getVenue());
        dto.setShowTime(show.getShowTime());
        dto.setContent(show.getContent());
        dto.setCreatedAt(show.getCreatedAt());

        if (show.getPublisher() != null)
        {
            dto.setPublisherNickname(show.getPublisher().getNickname());
            dto.setPublisherAvatar(show.getPublisher().getAvatarUrl());
        }

        // 添加点赞信息
        AppUser currentUser = null;
        if (principal != null) {
            currentUser = userService.findByNickname(principal.getName());
        }
        LikeStatusDTO likeStatus = likeService.getLikeStatus(currentUser, TargetType.SHOW_ANNOUNCEMENT, show.getId());
        dto.setLikeCount(likeStatus.getLikeCount());
        dto.setIsLiked(likeStatus.getIsLiked());

        return dto;
    }
}