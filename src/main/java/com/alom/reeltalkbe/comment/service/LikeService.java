package com.alom.reeltalkbe.comment.service;

import com.alom.reeltalkbe.comment.domain.Comment;
import com.alom.reeltalkbe.comment.domain.Like;
import com.alom.reeltalkbe.comment.dto.CommentResponseDTO;
import com.alom.reeltalkbe.comment.dto.LikeDTO;
import com.alom.reeltalkbe.comment.repository.CommentRepository;
import com.alom.reeltalkbe.comment.repository.LikeRepository;
import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.user.domain.User;
import com.alom.reeltalkbe.user.dto.CustomUserDetails;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private LikeRepository likeRepository;

    public LikeDTO add(CustomUserDetails userDetails, Long commentId){

        if(userDetails == null){
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

        Long userId = userDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        if(likeRepository.findByUserAndComment(user, comment).isPresent()){
            throw new BaseException(BaseResponseStatus.EXIST_RATING);
        }

        Like like = Like.builder()
                .user(user)
                .comment(comment)
                .build();

        List<Like> likes = likeRepository.findByComment(comment);
        int likeCount = likes.size();
        comment.updateLikeCount(++likeCount);

        return new LikeDTO(like);
    }

    public void delete(CustomUserDetails userDetails, Long commentId){

        if(userDetails == null){
            throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

        Long userId = userDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

        if(likeRepository.findByUserAndComment(user, comment).isEmpty()){
            throw new BaseException(BaseResponseStatus.RATING_NOT_FOUND);
        }

        List<Like> likes = likeRepository.findByComment(comment);
        int likeCount = likes.size();
        comment.updateLikeCount(--likeCount);

        Like like = likeRepository.findByUserAndComment(user, comment).get();
        likeRepository.deleteById(like.getId());
    }
}
