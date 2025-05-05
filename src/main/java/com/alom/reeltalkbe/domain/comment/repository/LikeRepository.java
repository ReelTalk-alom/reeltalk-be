package com.alom.reeltalkbe.domain.comment.repository;

import com.alom.reeltalkbe.domain.comment.domain.Comment;
import com.alom.reeltalkbe.domain.comment.domain.Like;
import com.alom.reeltalkbe.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
        Optional<Like> findByUserAndComment(User user, Comment comment);
        List<Like> findByComment(Comment comment);
}
