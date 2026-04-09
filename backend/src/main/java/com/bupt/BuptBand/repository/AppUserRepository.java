package com.bupt.BuptBand.repository;

import com.bupt.BuptBand.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long>
{
    //继承了JpaRepository 后，自动拥有了增删改查的所有功能

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<AppUser> findByNickname(String nickname);
}


