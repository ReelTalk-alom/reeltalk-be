package com.alom.reeltalkbe.talk.service;


import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import com.alom.reeltalkbe.content.repository.ContentRepository;
import com.alom.reeltalkbe.talk.domain.TalkMessage;
import com.alom.reeltalkbe.talk.dto.TalkMessageDto;
import com.alom.reeltalkbe.talk.repository.TalkMessageRepository;
import com.alom.reeltalkbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TalkService {
    private final TalkMessageRepository talkMessageRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public List<TalkMessage> getMessagesByContentId(Long contendId) {
        return talkMessageRepository.findAllByContentId(contendId);
    }

    public TalkMessage saveTalkMessage(TalkMessageDto talkMessageDto) {
        talkMessageDto.setUser(userRepository.findById(talkMessageDto.getUserId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_USER)));

        return talkMessageRepository.save(
                TalkMessage.of(
                        talkMessageDto,
                        contentRepository.findById(talkMessageDto.getContentId()).orElseThrow(() -> new BaseException(BaseResponseStatus.CONTENT_NOT_FOUND))
                )
        );
    }

    public TalkMessage updateTalkMessage(TalkMessageDto talkMessageDto) {
        TalkMessage talkMessage = talkMessageRepository.findById(talkMessageDto.getMessageId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MESSAGE_NOT_FOUND));

        throwExceptionIfNotEqualUser(talkMessage, talkMessageDto);

        talkMessage.updateMessage(talkMessageDto);
        return talkMessageRepository.save(talkMessage);
    }

    public void deleteTalkMessage(TalkMessageDto talkMessageDto) {
        TalkMessage talkMessage = talkMessageRepository.findById(talkMessageDto.getMessageId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.MESSAGE_NOT_FOUND));

        throwExceptionIfNotEqualUser(talkMessage, talkMessageDto);

        talkMessageRepository.deleteById(talkMessageDto.getMessageId());
    }

    private void throwExceptionIfNotEqualUser(TalkMessage message, TalkMessageDto dto) {
        if(!message.getUser().getId().equals(dto.getUserId())) {
            throw new BaseException(BaseResponseStatus.NOT_YOUR_MESSAGE);
        }
    }
}
