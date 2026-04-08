package com.bupt.BuptBand.dto;

import lombok.Data;

@Data
public class RecruitmentCreateRequest
{
    private String title;
    private String position;
    private String style;
    private String requirements;
    private String detail;
    private String contactInformation;
    private Long publisherId;
}