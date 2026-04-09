package com.bupt.BuptBand.controller;

import com.bupt.BuptBand.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController
{

    private final FileService fileService;

    public FileController(FileService fileService)
    {
        this.fileService = fileService;
    }

    @PostMapping("/upload/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file)
    {
        try
        {
            if (file.isEmpty()) return ResponseEntity.badRequest().body("文件不能为空喵");

            //执行处理并保存
            String avatarUrl = fileService.processAndSaveAvatar(file);

            //返回处理后的访问路径
            return ResponseEntity.ok(avatarUrl);
        } catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("图片处理失败: " + e.getMessage());
        }
    }
}