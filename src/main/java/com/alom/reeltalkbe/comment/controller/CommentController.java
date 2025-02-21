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
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/reviews/{reviewId}")
    public BaseResponse<?> getComment(@PathVariable(required = true, name="reviewId") Long reviewId){
        return new BaseResponse<>(commentService.getByReview(reviewId));

    }

    @PostMapping("/reviews/{reviewId}")
    public BaseResponse<?> postComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody CommentRequestDTO commentRequestDTO){

        return new BaseResponse<>(commentService.add(userDetails, reviewId, commentRequestDTO));

    }


    @PutMapping("/reviews/{reviewId}")
    public BaseResponse<?> updateComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                         @RequestParam(required = true, name = "commentId") Long commentId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody CommentRequestDTO commentRequestDTO){

        return new BaseResponse<>(commentService.update(userDetails, commentId, reviewId, commentRequestDTO));

    }


    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse<?> deleteComment(@PathVariable(required = true, name = "reviewId") Long reviewId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestParam(required = true, name = "commentId") Long commentId) {

        commentService.delete(userDetails, commentId, reviewId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);

    }

    @PostMapping("/{commentId}")
    public BaseResponse<?> updateLike(@PathVariable(required = true, name="commentId") Long commentId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        return new BaseResponse<>(commentService.updateLike(userDetails, commentId));
    }

    
}
