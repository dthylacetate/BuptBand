package com.bupt.BuptBand.service;

import com.bupt.BuptBand.dto.RecruitmentCreateRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.model.Recruitment;
import com.bupt.BuptBand.repository.AppUserRepository;
import com.bupt.BuptBand.repository.RecruitmentRepository;
import org.springframework.stereotype.Service;

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

        // 建立多对一的关联
        recruitment.setPublisher(publisher);

        return recruitmentRepository.save(recruitment);
    }

    public List<Recruitment> findAllRecruitments()
    {
        return recruitmentRepository.findAll();
    }

}