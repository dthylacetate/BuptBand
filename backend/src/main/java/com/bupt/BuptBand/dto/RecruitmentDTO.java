package com.bupt.BuptBand.dto;

import com.bupt.BuptBand.model.RecruitmentType;
import lombok.Data;

@Data
public class RecruitmentDTO
{
    private Long id;                // 数据库生成的 ID，展示时很重要
    private String title;
    private String position;
    private String style;
    private String requirements;
    private String detail;
    private String contactInformation;
    private RecruitmentType type;
    private String PublisherAvatar;

    // 不把整个AppUser传出去，只传一个昵称
    private String publisherNickname;

    private Integer likeCount;
    private Boolean isLiked;

}