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


    public List<Recruitment> filterRecruitments(RecruitmentType type, String position, String keyword)
    {
        return recruitmentRepository.findAll((root, query, criteriaBuilder) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 原有的精确筛选 (AND 关系)
            if (type != null)
            {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }
            if (position != null && !position.isEmpty() && !position.equalsIgnoreCase("全部"))
            {
                predicates.add(criteriaBuilder.equal(root.get("position"), position));
            }

            // 2. 新增的全局模糊搜索 (OR 关系)
            if (keyword != null && !keyword.trim().isEmpty())
            {
                String likePattern = "%" + keyword + "%"; // 构造模糊匹配字符串

                // 这里需要创建一个“内部 OR 组”
                Predicate titlePredicate = criteriaBuilder.like(root.get("title"), likePattern);
                Predicate detailPredicate = criteriaBuilder.like(root.get("detail"), likePattern);
                Predicate stylePredicate = criteriaBuilder.like(root.get("style"), likePattern);

                // 高级操作：搜索关联表中的发布者昵称
                // 我们需要 join 进来 publisher 实体
                Predicate nicknamePredicate = criteriaBuilder.like(root.join("publisher").get("nickname"), likePattern);

                // 把这些模糊搜索条件用 OR 连起来
                Predicate searchPredicate = criteriaBuilder.or(titlePredicate, detailPredicate, stylePredicate, nicknamePredicate);

                // 把这整个 OR 块作为一条 AND 条件加入主列表
                predicates.add(searchPredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }





    public Recruitment createRecruitmentWithUser(RecruitmentCreateRequest request, String nickname)
    {
        //根据昵称找到发布者对象
        AppUser publisher = appUserRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("错误：发布者身份异常"));

        //将 DTO 的数据填充到 Entity
        Recruitment recruitment = new Recruitment();
        recruitment.setTitle(request.getTitle());
        recruitment.setPosition(request.getPosition());
        recruitment.setStyle(request.getStyle());
        recruitment.setRequirements(request.getRequirements());
        recruitment.setDetail(request.getDetail());
        recruitment.setContactInformation(request.getContactInformation());
        recruitment.setType(request.getType());

        //手动建立关联
        recruitment.setPublisher(publisher);

        return recruitmentRepository.save(recruitment);
    }


    public void deleteRecruitment(Long id, String currentNickname)
    {
        //查找帖子，找不到直接抛异常
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("错误：该招募帖不存在"));

        //拿帖子的发布者昵称和当前 Token 里的昵称比对
        if (!recruitment.getPublisher().getNickname().equals(currentNickname))
        {

            throw new RuntimeException("非法操作：你只能删除自己发布的招募帖！");
        }

        recruitmentRepository.delete(recruitment);
    }

    public List<Recruitment> findMyRecruitments(String nickname)
    {

        return recruitmentRepository.findByPublisherNickname(nickname);
    }
    //查询单个帖子用ID
    public Recruitment findRecruitmentById(Long id)
    {
        return recruitmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("该招募贴已下架或不存在喵"));
    }





    public List<Recruitment> findAllRecruitments()
    {
        return recruitmentRepository.findAll();
    }

}