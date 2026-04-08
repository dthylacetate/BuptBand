package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.UserCreateRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.repository.AppUserRepository;
import com.bupt.BuptBand.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                 //这是一个RESTful风格的接口，返回json
@RequestMapping("/api/users")       //基础路径

public class UserController
{
    private final AppUserService appUserService;
    public UserController(AppUserService appUserService)
    {
        this.appUserService = appUserService;
    }


    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody UserCreateRequest request)
    {
        AppUser savedUser = appUserService.registerUser(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @GetMapping //当用户访问GET /api/users 时触发
    public List<AppUser> getAllUsers()
    {
        return appUserService.findAllUsers();
    }
}