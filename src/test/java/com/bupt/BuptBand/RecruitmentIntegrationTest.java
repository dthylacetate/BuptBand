package com.bupt.BuptBand;

import com.bupt.BuptBand.model.RecruitmentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 重点：测试跑完后自动回滚数据库，保证数据库永远是干净的
class RecruitmentIntegrationTest
{

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndFilterRecruitments() throws Exception
    {
        // 1. 先发布一个“招募贝斯手”的帖子
        String recruitment1 = """
           {
               "title": "金属乐队招贝斯",
               "position": "贝斯",
               "type": "BAND_SEEKING_MEMBER",
               "publisherId": 1
           }
           """;

        mockMvc.perform(post("/api/recruitments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recruitment1))
                .andExpect(status().isCreated());

        // 2. 再发布一个“吉他手求组队”的帖子
        String recruitment2 = """
           {
               "title": "大一吉他求带",
               "position": "吉他",
               "type": "MEMBER_SEEKING_BAND",
               "publisherId": 1
           }
           """;

        mockMvc.perform(post("/api/recruitments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recruitment2))
                .andExpect(status().isCreated());

        // --- 开始测试筛选功能 ---

        // 测试 A：只筛选“招募乐手”类型
        // 期望：只能看到“金属乐队招贝斯”，看不到“吉他手求组队”
        mockMvc.perform(get("/api/recruitments")
                        .param("type", "BAND_SEEKING_MEMBER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[?(@.type == 'MEMBER_SEEKING_BAND')]").isEmpty());

        // 测试 B：组合筛选（求组队 + 吉他）
        mockMvc.perform(get("/api/recruitments")
                        .param("type", "MEMBER_SEEKING_BAND")
                        .param("position", "吉他"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("大一吉他求带"));

        // 测试 C：筛选“全部”乐器
        // 期望：即使传了“全部”，后端也能正确处理，不崩溃且返回数据
        mockMvc.perform(get("/api/recruitments")
                        .param("position", "全部"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(2)));
    }
}