    package com.alom.reeltalkbe.domain.comment.service;


    import com.alom.reeltalkbe.domain.comment.dto.CommentResponseDTO;
    import com.alom.reeltalkbe.domain.comment.dto.CommentRequestDTO;
    import com.alom.reeltalkbe.domain.comment.domain.Comment;
    import com.alom.reeltalkbe.domain.comment.domain.Like;
    import com.alom.reeltalkbe.domain.comment.repository.CommentRepository;
    import com.alom.reeltalkbe.domain.comment.repository.LikeRepository;
    import com.alom.reeltalkbe.common.exception.BaseException;
    import com.alom.reeltalkbe.common.exception.BaseResponseStatus;
    import com.alom.reeltalkbe.domain.review.domain.Review;
    import com.alom.reeltalkbe.domain.review.repository.ReviewRepository;
    import com.alom.reeltalkbe.domain.user.domain.User;
    import com.alom.reeltalkbe.domain.user.dto.CustomUserDetails;
    import com.alom.reeltalkbe.domain.user.repository.UserRepository;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class CommentService {

        private final UserRepository userRepository;
        private final ReviewRepository reviewRepository;
        private final CommentRepository commentRepository;
        private final LikeRepository likeRepository;

        @Transactional(readOnly = true)
        public List<CommentResponseDTO> getByReview(long reviewId){
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_REVIEW));

            List<Comment> comments = commentRepository.findByReview(review);

            return (List<CommentResponseDTO>) comments.stream()
                    .map(CommentResponseDTO::new)
                    .collect(Collectors.toList());

        }

        @Transactional
        public CommentResponseDTO add(CustomUserDetails userDetails, Long reviewId, CommentRequestDTO commentParamDTO){
          User user = getUser(userDetails);

          Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new  BaseException(BaseResponseStatus.INVALID_REVIEW));


            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content(commentParamDTO.getContent())
                    .likeCount(0)
                    .build();

            return new CommentResponseDTO(commentRepository.save(comment));
        }



      @Transactional
        public CommentResponseDTO update(CustomUserDetails userDetails, Long commentId, Long reviewId, CommentRequestDTO commentRequestDTO) {
        User user = getUser(userDetails);

        Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));


            if (!comment.getUser().getId().equals(user.getId())) {
                throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
            }
            if (!comment.getReview().getId().equals(reviewId)) {
                throw new BaseException(BaseResponseStatus.INVALID_REVIEW);
            }

            comment.updateContent(commentRequestDTO.getContent());
            return new CommentResponseDTO(commentRepository.save(comment));

        }
        
        @Transactional
        public void delete(CustomUserDetails userDetails, Long commentId, Long reviewId){
          User user = getUser(userDetails);

          Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

            if (!comment.getUser().getId().equals(user.getId())) {
                throw new BaseException(BaseResponseStatus.INVALID_MEMBER);
            }
            if (!comment.getReview().getId().equals(reviewId)) {
                throw new BaseException(BaseResponseStatus.INVALID_REVIEW);
            }
            commentRepository.deleteById(commentId);
        }

        @Transactional
        public CommentResponseDTO updateLike(CustomUserDetails userDetails, Long commentId){

          User user = getUser(userDetails);

          Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COMMENT));

            Optional<Like> existingLike = likeRepository.findByUserAndComment(user, comment);

            if(existingLike.isPresent()){
                Like like = existingLike.get();
                likeRepository.deleteById(like.getId());

                List<Like> likes = likeRepository.findByComment(comment);
                int likeCount = likes.size();
                comment.updateLikeCount(likeCount);
                return new CommentResponseDTO(commentRepository.save(comment));
            }

            else{
                Like like = Like.builder()
                        .user(user)
                        .comment(comment)
                        .build();

                likeRepository.save(like);

                List<Like> likes = likeRepository.findByComment(comment);
                int likeCount = likes.size();
                comment.updateLikeCount(likeCount);
                return new CommentResponseDTO(commentRepository.save(comment));
            }

        }

      private User getUser(CustomUserDetails userDetails) {
        if(userDetails == null){
          throw new BaseException(BaseResponseStatus.FAIL_TOKEN_AUTHORIZATION);
        }

        Long userId = userDetails.getUserId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER));
        return user;
      }
    }
