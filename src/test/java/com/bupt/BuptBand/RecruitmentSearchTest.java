package com.bupt.BuptBand;

import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.Recruitment;
import com.bupt.BuptBand.model.RecruitmentType;
import com.bupt.BuptBand.repository.AppUserRepository;
import com.bupt.BuptBand.repository.RecruitmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 保证每个测试后数据库自动回滚
class RecruitmentSearchTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @BeforeEach
    void setUp()
    {
        // 1. 生成 10 个不同的用户
        List<AppUser> users = new ArrayList<>();
        String[] nicknames = {"小明", "老王", "阿强", "贝斯王", "键盘侠", "鼓手张", "吉他李", "主唱赵", "后期周", "乐评孙"};

        for (int i = 0; i < 10; i++)
        {
            AppUser user = new AppUser();
            user.setNickname(nicknames[i]);
            user.setInstrument("Instrument" + i);
            user.setCampus("西土城");
            users.add(appUserRepository.save(user));
        }

        // 2. 生成不同内容的测试招募帖
        // 帖子 A：标题含关键词
        createRecruitment("急招金属吉他手", "Guitar", "Heavy Metal", "招募高手", users.get(0), RecruitmentType.BAND_SEEKING_MEMBER);

        // 帖子 B：详情含关键词
        createRecruitment("北邮校友组队", "Vocal", "Pop", "要求是金属嗓音", users.get(1), RecruitmentType.MEMBER_SEEKING_BAND);

        // 帖子 C：风格含关键词
        createRecruitment("周五排练", "Drums", "Nu-Metal", "随意玩玩", users.get(2), RecruitmentType.BAND_SEEKING_MEMBER);

        // 帖子 D：发布者昵称含关键词 (发布者是“贝斯王”)
        createRecruitment("找个鼓手", "Drums", "Rock", "排练室见", users.get(3), RecruitmentType.BAND_SEEKING_MEMBER);
    }

    private void createRecruitment(String title, String pos, String style, String detail, AppUser publisher, RecruitmentType type)
    {
        Recruitment r = new Recruitment();
        r.setTitle(title);
        r.setPosition(pos);
        r.setStyle(style);
        r.setDetail(detail);
        r.setPublisher(publisher);
        r.setType(type);
        r.setContactInformation("WeChat: test");
        recruitmentRepository.save(r);
    }

    @Test
    void testGlobalFuzzySearch() throws Exception
    {
        // 场景 1：搜索标题中的“金属”
        mockMvc.perform(get("/api/recruitments").param("keyword", "金属"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].title", containsString("金属")));

        // 场景 2：搜索详情中的“金属”（帖子 B 的详情里有“金属嗓音”）
        mockMvc.perform(get("/api/recruitments").param("keyword", "嗓音"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].detail", containsString("嗓音")));

        // 场景 3：搜索风格中的“Nu-Metal”
        mockMvc.perform(get("/api/recruitments").param("keyword", "Nu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].style", containsString("Metal")));

        // 场景 4：搜索发布者昵称中的“贝斯”（发布者昵称是“贝斯王”）
        mockMvc.perform(get("/api/recruitments").param("keyword", "贝斯王"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$[0].publisherNickname", is("贝斯王")));

        // 场景 5：组合测试（筛选“招募” + 搜索“吉他”）
        mockMvc.perform(get("/api/recruitments")
                        .param("type", "BAND_SEEKING_MEMBER")
                        .param("keyword", "吉他"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", containsString("吉他手")))
                .andExpect(jsonPath("$[0].type", is("BAND_SEEKING_MEMBER")));
    }
}