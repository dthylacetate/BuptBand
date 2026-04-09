package com.bupt.BuptBand.model;

/*
  用枚举来限制类型，防止前端传乱七八糟的字符串
 */

public enum RecruitmentType
{
    BAND_SEEKING_MEMBER, // 队找人
    MEMBER_SEEKING_BAND  // 人找队
}