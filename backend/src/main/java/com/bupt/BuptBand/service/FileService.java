package com.bupt.BuptBand.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    // 获取项目根目录，并在其下建立 uploads/avatars
    // 这样文件夹就会出现在你的 src、pom.xml 平级的位置
    private final String uploadPath = "uploads/avatars/";

    public String processAndSaveAvatar(MultipartFile file) throws IOException {
        // 获取绝对路径，用于 File 对象操作
        String absolutePath = System.getProperty("user.dir") + File.separator + uploadPath;

        File dir = new File(absolutePath);
        if (!dir.exists()) {
            dir.mkdirs(); // 自动创建文件夹
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        File destFile = new File(absolutePath + fileName);

        // 居中裁剪并保存
        Thumbnails.of(file.getInputStream())
                .size(200, 200)
                .crop(Positions.CENTER)
                .outputFormat("jpg")
                .outputQuality(0.85f)
                .toFile(destFile);

        // 返回给前端的虚拟路径，始终以 /uploads/ 开头
        return "/uploads/avatars/" + fileName;
    }
}