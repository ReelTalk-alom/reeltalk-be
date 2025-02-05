package com.alom.reeltalkbe.controller;

import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/image")
public class S3Controller {

    private final S3UploadService s3UploadService;

    @GetMapping
    public BaseResponse<String> getImage() {
        String url = null;
        return new BaseResponse<>(url);
    }

    @PostMapping
    public BaseResponse<String> uploadImage(MultipartFile multipartFile) {
        String url = s3UploadService.uploadFile(multipartFile);
        return new BaseResponse<>(url);
    }

    @DeleteMapping
    public BaseResponse<String> deleteFile(@RequestParam String filename) {
        String url = s3UploadService.deleteFile(filename);
        return new BaseResponse<>(url);
    }
}