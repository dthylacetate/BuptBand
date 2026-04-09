package com.bupt.BuptBand.service;

import com.bupt.BuptBand.dto.LoginRequest;
import com.bupt.BuptBand.dto.RegistrationRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.repository.AppUserRepository;
import com.bupt.BuptBand.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService
{
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    // 构造器注入三个零件
    public AppUserService(AppUserRepository appUserRepository,
                          EmailService emailService,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil)
    {
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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



    // 登录
    public String login(LoginRequest request)
    {
        //查找用户
        AppUser user = appUserRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new RuntimeException("错误：该乐手尚未注册"));

        // 校验密码（注意：不能用 ==，必须用 matches 方法）
        // matches(明文密码, 数据库里的加密密码)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("错误：密码不正确");
        }


        return jwtUtil.generateToken(user.getNickname());
    }



    public AppUser updateUserProfile(String nickname, AppUser updateData)
    {
        // 找到这个人
        AppUser user = appUserRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("错误：用户不存在"));

        // 更新名片信息（如果前端传了的话）
        if (updateData.getInstrument() != null)
        {
            user.setInstrument(updateData.getInstrument());
        }
        if (updateData.getCampus() != null)
        {
            user.setCampus(updateData.getCampus());
        }
        if (updateData.getBio() != null)
        {
            user.setBio(updateData.getBio());
        }
        if (updateData.getPlayingYears() != null)
        {
            user.setPlayingYears(updateData.getPlayingYears());
        }

        // 存回数据库
        return appUserRepository.save(user);
    }






    public List<AppUser> findAllUsers()
    {
        return appUserRepository.findAll();
    }





}