package com.bupt.BuptBand.config;

import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer
{

    @Bean
    CommandLineRunner initDatabase(AppUserRepository repository)
    {
        return args ->
        {
            if (repository.count() == 0)
            {
                // 初始化第一个用户：阿强
                AppUser user1 = new AppUser();
                user1.setNickname("阿强");
                user1.setEmail("aqiang@bupt.edu.cn");
                // 注意：初始化数据的密码也建议加密，或者先随便填个字符串
                user1.setPassword("123456");
                user1.setInstrument("Bass");
                user1.setCampus("西土城");
                user1.setPlayingYears(5);
                user1.setBio("内向金属人");
                repository.save(user1);

                // 初始化第二个用户：老王
                AppUser user2 = new AppUser();
                user2.setNickname("老王");
                user2.setEmail("laowang@bupt.edu.cn");
                user2.setPassword("123456");
                user2.setInstrument("Keyboard");
                user2.setCampus("沙河");
                user2.setPlayingYears(3);
                user2.setBio("氛围吉他");
                repository.save(user2);

                System.out.println(">>>> 预置测试乐手数据已注入");
            }
        };
    }

}
