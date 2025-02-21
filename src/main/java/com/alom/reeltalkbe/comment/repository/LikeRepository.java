package com.alom.reeltalkbe.comment.repository;

import com.alom.reeltalkbe.comment.entity.Comment;
import com.alom.reeltalkbe.comment.entity.Like;
import com.alom.reeltalkbe.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
        Optional<Like> findByUserAndComment(User user, Comment comment);
        List<Like> findByComment(Comment comment);
}
