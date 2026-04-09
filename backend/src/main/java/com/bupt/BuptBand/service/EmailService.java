package com.bupt.BuptBand.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailService
{
    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // 定义 Redis 前缀，方便以后在数据库里管理
    private static final String REDIS_CODE_PREFIX = "reg_code:";

    public EmailService(JavaMailSender mailSender, StringRedisTemplate redisTemplate)
    {
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
    }

    public void sendVerificationCode(String toEmail)
    {
        // 生成 6 位随机验证码
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 100000));

        //存入 Redis，并设置 5 分钟后自动过期
        // key 是 "reg_code:xxx@example.com"，value 是验证码
        redisTemplate.opsForValue().set(
                REDIS_CODE_PREFIX + toEmail,
                code,
                30,
                TimeUnit.MINUTES
        );

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("BuptBand喵 <" + fromEmail + ">");
        message.setTo(toEmail);
        message.setSubject("BuptBand 注册验证码");
        message.setText("宝宝你的验证码是：" + code + "，有效期 30 分钟。请尽快完成注册喵。");

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String inputCode) {

        if (email == null || inputCode == null) {
            System.out.println("### 验证失败：Email 或验证码为空 ###");
            return false;
        }


        // 强行去除首尾空格
        String cleanEmail = email.trim();
        String cleanInputCode = inputCode.trim();
        String redisKey = REDIS_CODE_PREFIX + cleanEmail;

        // 从 Redis 取值
        String savedCode = redisTemplate.opsForValue().get(redisKey);

        // --- 调试打印
        System.out.println("### 正在验证 ###");
        System.out.println("待查 Email: [" + cleanEmail + "]");
        System.out.println("Redis Key: [" + redisKey + "]");
        System.out.println("Redis 中的验证码: [" + savedCode + "]");
        System.out.println("用户输入的验证码: [" + cleanInputCode + "]");
        // ----------------------------------------

        if (savedCode == null) {
            System.out.println("验证失败：Redis 中找不到该 Key 或已过期");
            return false;
        }

        if (savedCode.equals(cleanInputCode)) {
            redisTemplate.delete(redisKey);
            System.out.println("验证成功！已删除 Redis Key");
            return true;
        }

        System.out.println("验证失败：验证码不一致");
        return false;
    }
}