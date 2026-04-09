package com.bupt.BuptBand.dto;

import com.bupt.BuptBand.model.DiscussionSection;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DiscussionPostDTO
{
    private Long id;
    private String title;
    private String content;
    private DiscussionSection section;
    private String publisherNickname;
    private String publisherAvatar;
    private LocalDateTime createdAt;
}