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
}