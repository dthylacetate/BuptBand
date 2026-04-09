package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.RecruitmentCreateRequest;
import com.bupt.BuptBand.dto.RecruitmentDTO;
import com.bupt.BuptBand.model.Recruitment;
import com.bupt.BuptBand.model.RecruitmentType;
import com.bupt.BuptBand.service.RecruitmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recruitments")
public class RecruitmentController
{
    private final RecruitmentService recruitmentService;

    // 构造器注入
    public RecruitmentController(RecruitmentService recruitmentService)
    {
        this.recruitmentService = recruitmentService;
    }

    // 发布招募帖的接口 (POST)
    @PostMapping
    public ResponseEntity<RecruitmentDTO> createRecruitment(@RequestBody RecruitmentCreateRequest request)
    {

        String currentNickname = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 调用Service
        Recruitment savedRecruitment = recruitmentService.createRecruitmentWithUser(request, currentNickname);

        // 转化为DTO
        RecruitmentDTO response = convertToDTO(savedRecruitment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 查看所有招募帖的接口
    @GetMapping
    public List<RecruitmentDTO> getRecruitments(
            @RequestParam(required = false) RecruitmentType type,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String keyword)
    {
        // 调用带参数的筛选方法
        return recruitmentService.filterRecruitments(type, position,keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruitment(@PathVariable Long id)
    {
        // 获取当前登录乐手的昵称
        String currentNickname = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        recruitmentService.deleteRecruitment(id, currentNickname);

        return ResponseEntity.ok("招募帖已成功下架");
    }


    // 负责把 Entity 转成 DTO
    private RecruitmentDTO convertToDTO(Recruitment recruitment)
    {
        RecruitmentDTO dto = new RecruitmentDTO();
        dto.setId(recruitment.getId());
        dto.setTitle(recruitment.getTitle());
        dto.setPosition(recruitment.getPosition());
        dto.setStyle(recruitment.getStyle());
        dto.setRequirements(recruitment.getRequirements());
        dto.setDetail(recruitment.getDetail());
        dto.setContactInformation(recruitment.getContactInformation());
        dto.setType(recruitment.getType());
        // 把Publisher对象简化成一个简单的昵称字符串
        if (recruitment.getPublisher() != null)
        {
            dto.setPublisherNickname(recruitment.getPublisher().getNickname());
        }

        return dto;
    }
}