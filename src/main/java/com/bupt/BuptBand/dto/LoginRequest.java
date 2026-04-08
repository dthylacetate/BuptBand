package com.bupt.BuptBand.dto;

import lombok.Data;

@Data
public class LoginRequest
{
    private String nickname;
    private String password;
}