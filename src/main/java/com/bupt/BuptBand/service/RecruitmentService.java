package com.bupt.BuptBand.service;

import com.bupt.BuptBand.dto.RecruitmentCreateRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.Recruitment;
import com.bupt.BuptBand.model.RecruitmentType;
import com.bupt.BuptBand.repository.AppUserRepository;
import com.bupt.BuptBand.repository.RecruitmentRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


@Service
public class RecruitmentService
{
    private final RecruitmentRepository recruitmentRepository;
    private final AppUserRepository appUserRepository;

    public RecruitmentService(RecruitmentRepository recruitmentRepository, AppUserRepository appUserRepository)
    {
        this.recruitmentRepository = recruitmentRepository;
        this.appUserRepository = appUserRepository;
    }


    public List<Recruitment> filterRecruitments(RecruitmentType type, String position)
    {
        // 这里使用了 Lambda 表达式实现 Specification 接口
        return recruitmentRepository.findAll((root, query, criteriaBuilder) ->
        {
            // 这里的 Predicate 必须是 jakarta.persistence.criteria.Predicate
            List<Predicate> predicates = new ArrayList<>();

            // 1. 类型筛选
            if (type != null)
            {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            // 2. 位置/乐器筛选
            if (position != null && !position.isEmpty() && !position.equalsIgnoreCase("全部"))
            {
                predicates.add(criteriaBuilder.equal(root.get("position"), position));
            }

            // 最终合并所有条件
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }





    public Recruitment createRecruitment(RecruitmentCreateRequest request)
    {
        // 1核心逻辑：去仓库里根据前端传来的 ID 把那个乐手“捞”出来
        AppUser publisher = appUserRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("错误：该乐手 ID 不存在，无法发布招募"));

        //  组装实体
        Recruitment recruitment = new Recruitment();
        recruitment.setTitle(request.getTitle());
        recruitment.setPosition(request.getPosition());
        recruitment.setStyle(request.getStyle());
        recruitment.setRequirements(request.getRequirements());
        recruitment.setDetail(request.getDetail());
        recruitment.setContactInformation(request.getContactInformation());
        recruitment.setType(request.getType());

        // 建立多对一的关联
        recruitment.setPublisher(publisher);

        return recruitmentRepository.save(recruitment);
    }

    public List<Recruitment> findAllRecruitments()
    {
        return recruitmentRepository.findAll();
    }

}