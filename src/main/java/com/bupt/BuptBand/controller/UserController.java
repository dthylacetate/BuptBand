package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController                 //这是一个RESTful风格的接口，返回json
@RequestMapping("/api/users")       //基础路径
public class UserController
{
    private final AppUserRepository appUserRepository;
    public UserController(AppUserRepository appUserRepository)
    {
        this.appUserRepository = appUserRepository;
    }

    @GetMapping //当用户访问GET /api/users 时触发
    public List<AppUser> getAllUsers()
    {
        return appUserRepository.findAll();
    }
}