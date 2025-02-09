package com.alom.reeltalkbe.talk.service;


import com.alom.reeltalkbe.talk.domain.TalkMessage;
import com.alom.reeltalkbe.talk.dto.TalkMessageDto;
import com.alom.reeltalkbe.talk.repository.TalkMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TalkService {
    private final TalkMessageRepository chatMessageRepository;

    public TalkMessage saveMessage(TalkMessageDto chatMessage) {

        // todo : content id로 content 객체 구해서 talkMessage 객체 생성 후 저장

        return chatMessageRepository.save(null);
    }

    public List<TalkMessage> getMessagesByContentId(Long contendId) {
        return chatMessageRepository.findAllByContentId(contendId);
    }



}
