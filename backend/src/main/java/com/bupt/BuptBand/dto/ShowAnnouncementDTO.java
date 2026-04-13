package com.bupt.BuptBand.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShowAnnouncementDTO
{
    private Long id;
    private String title;
    private String venue;
    private LocalDateTime showTime;
    private String content;
    private String publisherNickname;
    private String publisherAvatar;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private Boolean isLiked;
}