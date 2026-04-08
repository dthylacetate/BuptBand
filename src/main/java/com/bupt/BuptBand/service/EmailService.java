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
                5,
                TimeUnit.MINUTES
        );

        // 发送邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("BuptBand喵 <" + fromEmail + ">");
        message.setTo(toEmail);
        message.setSubject("BuptBand 注册验证码");
        message.setText("宝宝你的验证码是：" + code + "，有效期 5 分钟。请尽快完成注册喵。");

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String inputCode)
    {
        // 从 Redis 中取出验证码
        String savedCode = redisTemplate.opsForValue().get(REDIS_CODE_PREFIX + email);

        // 如果 savedCode 为空，说明验证码已过期或从未发送
        if (savedCode == null)
        {
            return false;
        }

        // 比对成功后，建议立即手动删除验证码，防止被重复利用（专业做法）
        if (savedCode.equals(inputCode))
        {
            redisTemplate.delete(REDIS_CODE_PREFIX + email);
            return true;
        }

        return false;
    }
}