package com.bupt.BuptBand.service;

import com.bupt.BuptBand.dto.UserCreateRequest;
import com.bupt.BuptBand.model.AppUser;
import com.bupt.BuptBand.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class AppUserService
{

    private final AppUserRepository appUserRepository;
    public AppUserService(AppUserRepository appUserRepository)
    {
        this.appUserRepository = appUserRepository;
    }


    //  Service方法只关注业务，来一个DTO，返回一个实体
    public AppUser registerUser(UserCreateRequest request)
    {
        AppUser newUser =new AppUser();
        newUser.setNickname(request.getNickname());
        newUser.setInstrument(request.getInstrument());
        newUser.setCampus(request.getCampus());
        newUser.setPlayingYears(request.getPlayingYears());
        newUser.setBio(request.getBio());

        return appUserRepository.save(newUser);
    }


    public List<AppUser> findAllUsers()
    {
        return appUserRepository.findAll();
    }

}
