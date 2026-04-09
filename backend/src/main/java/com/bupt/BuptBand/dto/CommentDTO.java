package com.bupt.BuptBand.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDTO
{
    private Long id;
    private String content;
    private String userNickname;
    private String userAvatar;
    private LocalDateTime createdAt;
}