package com.bupt.BuptBand.dto;
import lombok.Data;

@Data

public class UserCreateRequest
{
    private String nickname;
    private String instrument;
    private String campus;
    private Integer playingYears;
    private String bio;

}
