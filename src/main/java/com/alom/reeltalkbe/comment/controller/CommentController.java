package com.alom.reeltalkbe.comment.controller;


import com.alom.reeltalkbe.comment.dto.CommentRequestDTO;
import com.alom.reeltalkbe.comment.service.CommentService;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{reviewId}/comments")
    public BaseResponse<?> getComment(@PathVariable(required = true, name="reviewId") Long reviewId){
        return new BaseResponse<>(commentService.getByReview(reviewId));

    }

    @PostMapping("/{reviewId}/comments")
    public BaseResponse<?> postComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody CommentRequestDTO commentRequestDTO){

        return new BaseResponse<>(commentService.add(userDetails, reviewId, commentRequestDTO));

    }


    @PutMapping("/{reviewId}/comments/{commentId}")
    public BaseResponse<?> updateComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                         @PathVariable(required = true, name = "commentId") Long commentId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody CommentRequestDTO commentRequestDTO){

        return new BaseResponse<>(commentService.update(userDetails, commentId, reviewId, commentRequestDTO));

    }


    @DeleteMapping("/{reviewId}/comments/{commentId}")
    public BaseResponse<?> deleteComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                         @PathVariable(required = true, name = "commentId") Long commentId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {


        commentService.delete(userDetails, commentId, reviewId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);

    }

    @PutMapping("/{reviewId}/comments/{commentId}/like")
    public BaseResponse<?> updateLike(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                      @PathVariable(required = true, name="commentId") Long commentId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        return new BaseResponse<>(commentService.updateLike(userDetails, commentId));
    }

    
}
