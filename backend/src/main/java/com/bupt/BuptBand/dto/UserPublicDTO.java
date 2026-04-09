package com.bupt.BuptBand.dto;

import lombok.Data;

@Data
public class UserPublicDTO
{
    private String nickname;
    private String instrument;
    private Integer playingYears;
    private String bio;
    private String avatarUrl;
}