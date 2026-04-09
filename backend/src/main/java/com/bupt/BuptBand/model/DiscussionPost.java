package com.bupt.BuptBand.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class DiscussionPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private DiscussionSection section;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser publisher;

    private LocalDateTime createdAt = LocalDateTime.now();
}