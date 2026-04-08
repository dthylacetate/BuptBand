package com.bupt.BuptBand.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity                 //告诉spring 这是一张数据库表
@Table(name="Users")        //表名
@Data                       // Lombok自动生成 Getter/Setter
@NoArgsConstructor          //自动生成无参构造函数
@AllArgsConstructor         //自动生成全参构造函数


public class AppUser
{

    @Id     //主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //自增ID
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname; // 登录账号，必须唯一
    @Column(unique = true, nullable = false)
    private String email;    // 邮箱，也必须唯一
    @Column(nullable = false)
    private String password; // 存储加密后的哈希值

    //@Column(nullable = false)       //不能为空
    private String instrument;      //主打乐器
    private String campus;          //校区
    private Integer playingYears;   //琴龄

    @Column(length = 1000)
    private String bio;         //个人简介
    //后续再来添加剩余的字段，头像url之类的




}
