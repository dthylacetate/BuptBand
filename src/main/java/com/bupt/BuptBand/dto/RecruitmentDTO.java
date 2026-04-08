package com.bupt.BuptBand.dto;

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

    // 不把整个AppUser传出去，只传一个昵称
    private String publisherNickname;
}