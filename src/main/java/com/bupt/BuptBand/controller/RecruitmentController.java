package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.RecruitmentCreateRequest;
import com.bupt.BuptBand.dto.RecruitmentDTO;
import com.bupt.BuptBand.model.Recruitment;
import com.bupt.BuptBand.service.RecruitmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // 调用Service
        Recruitment savedRecruitment = recruitmentService.createRecruitment(request);

        // 转化为DTO
        RecruitmentDTO response = convertToDTO(savedRecruitment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 查看所有招募帖的接口
    @GetMapping
    public List<RecruitmentDTO> getAllRecruitments()
    {
        // 获取所有实体并统一转成 DTO 列表
        return recruitmentService.findAllRecruitments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

        // 把Publisher对象简化成一个简单的昵称字符串
        if (recruitment.getPublisher() != null)
        {
            dto.setPublisherNickname(recruitment.getPublisher().getNickname());
        }

        return dto;
    }
}