package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.LoginRequest;
import com.bupt.BuptBand.dto.RegistrationRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.service.AppUserService;
import com.bupt.BuptBand.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    private final AppUserService appUserService;
    private final EmailService emailService; // 注入邮件服务，用于发验证码

    public UserController(AppUserService appUserService, EmailService emailService)
    {
        this.appUserService = appUserService;
        this.emailService = emailService;
    }



     //用户在注册页面输入邮箱后，点击按钮调用这个接口

    @PostMapping("/send-code")
    public ResponseEntity<String> sendCode(@RequestParam String email)
    {
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("验证码已发送，请检查您的邮箱（未收到的话可以查看一下垃圾邮件）");
    }



      //用户填完昵称、密码、邮箱和验证码后，点击提交调用这个接口


    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody RegistrationRequest request)
    {
        // 这里的 request 已经是正确的 RegistrationRequest 类型了
        AppUser savedUser = appUserService.registerUser(request);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }



    // 在 UserController 中增加
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request)
    {
        try
        {
            String token = appUserService.login(request);
            // 把生成的 Token 甩给前端
            return ResponseEntity.ok(token);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PutMapping("/me")
    public ResponseEntity<AppUser> updateMyProfile(@RequestBody AppUser updateData)
    {
        // 从 Spring Security 的上下文中拿到 JwtAuthenticationFilter 塞进去的昵称
        String currentNickname = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 打印一下日志，看看是谁在修改资料
        System.out.println("正在更新乐手资料，昵称: " + currentNickname);

        AppUser updatedUser = appUserService.updateUserProfile(currentNickname, updateData);
        return ResponseEntity.ok(updatedUser);
    }







    @GetMapping
    public List<AppUser> getAllUsers()
    {
        return appUserService.findAllUsers();
    }
}