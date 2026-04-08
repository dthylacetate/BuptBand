package com.bupt.BuptBand.dto;

import lombok.Data;

@Data
public class RegistrationRequest
{
    private String nickname;
    private String password;
    private String email;
    private String verificationCode; // 前端传回来的 6 位验证码
}