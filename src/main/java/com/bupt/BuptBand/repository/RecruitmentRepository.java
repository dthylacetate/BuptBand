package com.bupt.BuptBand.repository;

import com.bupt.BuptBand.model.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository     //继承 JpaSpecificationExecutor 接口会赋予它处理动态条件的能力。
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>,JpaSpecificationExecutor<Recruitment>
{
}