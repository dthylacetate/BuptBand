package com.bupt.BuptBand.service;

import com.bupt.BuptBand.dto.RegistrationRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService
{
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // 构造器注入三个零件
    public AppUserService(AppUserRepository appUserRepository,
                          EmailService emailService,
                          PasswordEncoder passwordEncoder)
    {
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


     //带验证码校验的注册

    public AppUser registerUser(RegistrationRequest request)
    {
        // 验证码校验
        if (!emailService.verifyCode(request.getEmail(), request.getVerificationCode()))
        {
            throw new RuntimeException("错误：验证码不匹配或已过期");
        }

        // 查重校验（防止昵称或邮箱冲突）
        if (appUserRepository.existsByNickname(request.getNickname()))
        {
            throw new RuntimeException("错误：该昵称已被占用");
        }
        if (appUserRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("错误：该邮箱已注册过账号");
        }

        //  组装实体
        AppUser newUser = new AppUser();
        newUser.setNickname(request.getNickname());
        newUser.setEmail(request.getEmail());

        // 不存明文
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(encodedPassword);

        //  初始乐手名片信息（可选）
        // 注册时可以只传核心信息，后续再通过完善资料补全其余的字段如
        // newUser.setInstrument(request.getInstrument());

        return appUserRepository.save(newUser);
    }

    public List<AppUser> findAllUsers()
    {
        return appUserRepository.findAll();
    }
}