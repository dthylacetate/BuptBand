package com.bupt.BuptBand;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType; // 解决 MediaType 报错
import org.springframework.test.web.servlet.MockMvc;

// 以下是关键的静态导入，必须手动添加或让 IDEA 自动识别
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BuptBandApplicationTests
{

	@Autowired // 必须加这个注解，解决“从未分配”警告
	private MockMvc mockMvc;

	@Test
	void testCreateUser() throws Exception
	{
		String userJson = """
            {
                "nickname": "新加入的吉他手",
                "instrument": "Electric Guitar",
                "campus": "西土城",
                "playingYears": 2,
                "bio": "求大佬带"
            }
            """;

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isCreated()) // 验证 201 状态码
				.andExpect(jsonPath("$.nickname").value("新加入的吉他手"));
	}

	@Test
	void testCreateRecruitment() throws Exception
	{
		// 1. 准备数据：必须是一个数据库里真实存在的 publisherId
		// 因为你的 DataInitializer 会在启动时自动创建 ID 为 1 和 2 的用户
		String recruitmentJson = """
        {
            "title": "北邮首支后摇乐队招键盘",
            "position": "Keyboard",
            "style": "Post-Rock",
            "requirements": "会调音色，不排斥氛围铺底",
            "detail": "目前成员都是大二大三的，氛围很好",
            "contactInformation": "微信: xitucheng_player",
            "publisherId": 1
        }
        """;

		// 2. 发送 POST 请求
		mockMvc.perform(post("/api/recruitments")
						.contentType(MediaType.APPLICATION_JSON)
						.content(recruitmentJson))
				// 3. 验证结果
				.andExpect(status().isCreated()) // 期望返回 201
				.andExpect(jsonPath("$.title").value("北邮首支后摇乐队招键盘"))
				// 4. 核心验证：检查返回的 DTO 里，昵称是否根据 ID 自动查出来了
				.andExpect(jsonPath("$.publisherNickname").exists());
	}


}