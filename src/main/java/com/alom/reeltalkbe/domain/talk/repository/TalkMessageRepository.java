package com.alom.reeltalkbe.domain.talk.repository;

import com.alom.reeltalkbe.domain.talk.domain.TalkMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TalkMessageRepository extends JpaRepository<TalkMessage, Long> {
    List<TalkMessage> findAllByContentId(Long contentId);
}
