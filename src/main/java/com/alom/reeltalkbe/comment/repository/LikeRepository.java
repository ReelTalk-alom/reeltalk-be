package com.alom.reeltalkbe.comment.repository;

import com.alom.reeltalkbe.comment.domain.Comment;
import com.alom.reeltalkbe.comment.domain.Like;
import com.alom.reeltalkbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
        Optional<Like> findByUserAndComment(User user, Comment comment);
}
