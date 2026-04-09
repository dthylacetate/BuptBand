package com.bupt.BuptBand.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class ShowAnnouncement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;          // 演出标题
    private String venue;          // 演出地点

    private LocalDateTime showTime; // 演出具体时间（开演时间）

    @Column(columnDefinition = "TEXT")
    private String content;        // 演出详情/阵容介绍

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser publisher;     // 发布者

    private LocalDateTime createdAt = LocalDateTime.now();
}