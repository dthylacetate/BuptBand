package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.dto.LikeStatusDTO;
import com.bupt.BuptBand.dto.RecruitmentCreateRequest;
import com.bupt.BuptBand.dto.RecruitmentDTO;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.Recruitment;
import com.bupt.BuptBand.model.RecruitmentType;
import com.bupt.BuptBand.model.TargetType;
import com.bupt.BuptBand.service.AppUserService;
import com.bupt.BuptBand.service.LikeService;
import com.bupt.BuptBand.service.RecruitmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*") // 暂时允许所有来源，开发环境最省事
@RestController
@RequestMapping("/api/recruitments")
public class RecruitmentController
{
    private final RecruitmentService recruitmentService;
    private final LikeService likeService;
    private final AppUserService userService;

    // 构造器注入
    public RecruitmentController(RecruitmentService recruitmentService, LikeService likeService, AppUserService userService)
    {
        this.recruitmentService = recruitmentService;
        this.likeService = likeService;
        this.userService = userService;
    }

    // 发布招募帖的接口 (POST)
    @PostMapping
    public ResponseEntity<RecruitmentDTO> createRecruitment(@RequestBody RecruitmentCreateRequest request, Principal principal)
    {

        String currentNickname = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 调用Service
        Recruitment savedRecruitment = recruitmentService.createRecruitmentWithUser(request, currentNickname);

        // 转化为DTO
        RecruitmentDTO response = convertToDTO(savedRecruitment, principal);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 查看所有招募帖的接口
    @GetMapping
    public List<RecruitmentDTO> getRecruitments(
            @RequestParam(required = false) RecruitmentType type,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String keyword,
            Principal principal)
    {
        // 调用带参数的筛选方法
        return recruitmentService.filterRecruitments(type, position,keyword)
                .stream()
                .map(recruitment -> convertToDTO(recruitment, principal))
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





    @GetMapping("/my")
    public ResponseEntity<List<RecruitmentDTO>> getMyRecruitments(Principal principal)
    {
        // 从安全上下文获取当前人昵称
        String currentNickname = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //调用 Service 获取实体列表
        List<Recruitment> myItems = recruitmentService.findMyRecruitments(currentNickname);

        //转化为 DTO 列表返回
        List<RecruitmentDTO> response = myItems.stream()
                .map(recruitment -> convertToDTO(recruitment, principal))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "查询单个招募详情")
    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentDTO> getRecruitmentDetail(@PathVariable Long id, Principal principal)
    {

        Recruitment recruitment = recruitmentService.findRecruitmentById(id);
        return ResponseEntity.ok(convertToDTO(recruitment, principal));
    }



    @GetMapping("/user/{nickname}")
    public ResponseEntity<List<RecruitmentDTO>> getRecruitmentsByUser(@PathVariable String nickname, Principal principal)
    {
        List<Recruitment> list = recruitmentService.findByNickname(nickname);
        return ResponseEntity.ok(list.stream().map(recruitment -> convertToDTO(recruitment, principal)).collect(Collectors.toList()));
    }




    // 负责把 Entity 转成 DTO
    private RecruitmentDTO convertToDTO(Recruitment recruitment, Principal principal)
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
            dto.setPublisherAvatar(recruitment.getPublisher().getAvatarUrl());
        }

        // 添加点赞信息
        AppUser currentUser = null;
        if (principal != null) {
            currentUser = userService.findByNickname(principal.getName());
        }
        LikeStatusDTO likeStatus = likeService.getLikeStatus(currentUser, TargetType.RECRUITMENT, recruitment.getId());
        dto.setLikeCount(likeStatus.getLikeCount());
        dto.setIsLiked(likeStatus.getIsLiked());

        return dto;
    }
}