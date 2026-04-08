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
            //如果是空的，就随便塞点数据
            if(repository.count()==0)
            {
                repository.save(new AppUser(null, "贝斯", "Bass", "西土城", 5, "鸡柳金属"));
                repository.save(new AppUser(null, "黄毛键盘女", "Keyboard", "沙河", 3, "黄毛女"));
                System.out.println(">>>>初始测试数据已注入");
            }
        };
    }

}
