package com.learn.reggie.controller;

import com.learn.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("common")
@Slf4j
public class CommonController {

    @Value("${file.basepath}")
    private String basepath;

    @PostMapping("upload")
    public R<String> upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(
                originalFilename.lastIndexOf(".")
        );
        String filename = UUID.randomUUID().toString() + suffix;

        File baseFile = new File(basepath);
        if (!baseFile.exists()) {
            baseFile.mkdirs();
        }

        try {
            file.transferTo(new File(basepath + filename));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return R.success(filename);
    }

    @GetMapping("download")
    public void download(String name, HttpServletResponse response) {
        try {
            FileInputStream bis = new FileInputStream(basepath + name);
            ServletOutputStream os = response.getOutputStream();
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bis.read(bytes)) != -1) {
                os.write(bytes, 0, len);
                os.flush();
            }
            bis.close();
            os.close();
        } catch (Exception Exception) {
//            Exception.printStackTrace();
            log.error(Exception.getMessage());
        }
    }

}
