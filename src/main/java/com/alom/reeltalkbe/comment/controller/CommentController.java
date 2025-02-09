package com.alom.reeltalkbe.comment.controller;


import com.alom.reeltalkbe.comment.dto.CommentRequestDTO;
import com.alom.reeltalkbe.comment.dto.CommentResponseDTO;
import com.alom.reeltalkbe.comment.service.CommentService;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{reviewId}")
    public BaseResponse<?> getComment(@PathVariable(required = true, name="reviewId") Long reviewId){
        try{
            List<CommentResponseDTO> comments = commentService.getByReview(reviewId);
            return new BaseResponse<>(comments);
        } catch(Exception e){
            return new BaseResponse<>(BaseResponseStatus.COMMENT_NOT_FOUND);
        }
    }

    @PostMapping("/{reviewId}")
    public BaseResponse<?> postComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                       @RequestParam(required = true, name = "userId") Long userId, //getUserId 추가 후 삭제 예정
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                       CommentRequestDTO commentRequestDTO){
        try {
            //CommentResponseDTO comment = commentService.add(customUserDetails, reviewId, commentRequestDTO);
            CommentResponseDTO comment = commentService.add(userId, reviewId, commentRequestDTO);
            return new BaseResponse<>(comment);
        } catch(Exception e){
            return new BaseResponse<>(BaseResponseStatus.FAIL_COMMENT_POST);
        }
    }


    @PutMapping("/{reviewId}")
    public BaseResponse<?> updateComment(@RequestParam(required = true, name = "userID") Long userId,
                                         @RequestParam(required = true, name = "commentId") Long commentId, //getUserId 추가후 삭제 예정
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         CommentRequestDTO commentRequestDTO){
        try {
            //CommentResponseDTO comment = commentService.add(customUserDetails, reviewId, commentRequestDTO);
            CommentResponseDTO comment = commentService.update(userId, commentId, commentRequestDTO);
            return new BaseResponse<>(comment);
        } catch(Exception e){
            return new BaseResponse<>(BaseResponseStatus.FAIL_COMMENT_POST);
        }
    }


    @DeleteMapping("/{reviewId}")
    public BaseResponse<?> deleteComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                         @RequestParam(required = true, name = "userId") Long userId, //getUserId 추가후 삭제 예정
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestParam(required = true, name = "commentId") Long commentId) {

        try {
            //CommentResponseDTO comment = commentService.add(customUserDetails, reviewId, commentRequestDTO);
            commentService.delete(userId, commentId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(Exception e){
            return new BaseResponse<>(BaseResponseStatus.FAIL_COMMENT_DELETE);
        }
    }

    
}
