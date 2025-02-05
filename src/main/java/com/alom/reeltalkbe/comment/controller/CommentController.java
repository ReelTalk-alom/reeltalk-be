package com.alom.reeltalkbe.comment.controller;


import com.alom.reeltalkbe.comment.dto.CommentRequestDTO;
import com.alom.reeltalkbe.comment.dto.CommentResponseDTO;
import com.alom.reeltalkbe.comment.service.CommentService;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
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
            return new BaseResponse<>("FAIL");
        }
    }

    @PostMapping("/{reviewId}")
    public BaseResponse<?> postComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                       @RequestParam(required = true, name = "userId") Long userID,
                                       CommentRequestDTO commentRequestDTO){
        try {
            CommentResponseDTO comment = commentService.add(userID, reviewId, commentRequestDTO);
            return new BaseResponse<>(comment);
        } catch(Exception e){
            return new BaseResponse<>("Comment creation failed");
        }
    }


    @PutMapping("/{reviewId}")
    public BaseResponse<?> updateComment(@RequestParam(required = true, name = "userID") Long userId,
                                         @RequestParam(required = true, name = "commentId") Long commentId,
                                         CommentRequestDTO commentRequestDTO){
        try {
            CommentResponseDTO comment = commentService.update(userId, commentId, commentRequestDTO);
            return new BaseResponse<>(comment);
        } catch(Exception e){
            return new BaseResponse<>("Comment update failed");
        }
    }


    @DeleteMapping("/{reviewId}")
    public BaseResponse<?> deleteComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                         @PathVariable(required = true, name = "userId") Long userId,
                                         @RequestParam(required = true, name = "commentId") Long commentId) {

        try {
            commentService.delete(userId, commentId);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        } catch(Exception e){
            return new BaseResponse<>("Comment delete failed");
        }
    }

    
}
