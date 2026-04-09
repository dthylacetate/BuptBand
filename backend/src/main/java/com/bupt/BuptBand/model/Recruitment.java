package com.bupt.BuptBand.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recruitments")
@Data
public class Recruitment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;          // 招募标题
    private String position;       // 招募位置（如：贝斯手）
    private String style;          // 乐队风格
    private String requirements;   // 要求
    private String detail;          //详细介绍
    private String contactInformation;       //联系方式
    @Enumerated(EnumType.STRING)
    private RecruitmentType type;   // 帖子类型

    // 核心：多对一关联
    @ManyToOne(fetch = FetchType.LAZY) // 懒加载，只有用到用户信息时才会去查数据库，性能更好
    @JoinColumn(name = "publisher_id") // 数据库里的外键列名
    private AppUser publisher;
}