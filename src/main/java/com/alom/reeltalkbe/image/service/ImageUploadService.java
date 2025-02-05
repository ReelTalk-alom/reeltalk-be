package com.alom.reeltalkbe.image.service;

import com.alom.reeltalkbe.image.domain.Image;
import com.alom.reeltalkbe.image.repository.ImageRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    public String uploadFile(MultipartFile multipartFile){

        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }


        imageRepository.save(Image.builder().url(amazonS3.getUrl(bucket, fileName).toString()).build());

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //  "."의 존재 유무만 판단
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }


    public String deleteFile(String fileName) {
        System.out.println("삭제 요청 파일명: " + fileName);

        // S3에 파일이 존재하는지 확인
        boolean isExist = amazonS3.doesObjectExist(bucket, fileName);

        if (!isExist) {
            System.out.println("파일이 존재하지 않습니다: " + fileName);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다: " + fileName);
        }

        String originUrl = amazonS3.getUrl(bucket, fileName).toString();

        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
        System.out.println("삭제 완료: " + fileName);
        return originUrl;
    }
}