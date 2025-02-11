//package com.alom.reeltalkbe.image.controller;
//
//import com.alom.reeltalkbe.common.response.BaseResponse;
//import com.alom.reeltalkbe.image.service.ImageUploadService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/mypage/image")
//public class ImageController {
//
//    private final ImageUploadService imageUploadService;
//
//    @GetMapping
//    public BaseResponse<String> getImage() {
//        String url = null;
//        return new BaseResponse<>(url);
//    }
//
//    @PostMapping
//    public BaseResponse<String> uploadImage(MultipartFile multipartFile) {
//        String url = imageUploadService.uploadFile(multipartFile);
//        return new BaseResponse<>(url);
//    }
//
//    @DeleteMapping
//    public BaseResponse<String> deleteFile(@RequestParam Long id) {
//        String url = imageUploadService.deleteFile(id);
//        return new BaseResponse<>(url);
//    }
//}