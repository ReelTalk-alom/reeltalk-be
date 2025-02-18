package com.alom.reeltalkbe.comment.controller;


import com.alom.reeltalkbe.comment.service.LikeService;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public BaseResponse<?> addLike(@PathVariable(required = true, name="commentId") Long commentId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails){
        return new BaseResponse<>(likeService.add(userDetails, commentId));
    }

    @DeleteMapping
    public BaseResponse<?> deleteLike(@PathVariable(required = true, name="commentId") Long commentId,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        likeService.delete(userDetails, commentId);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
