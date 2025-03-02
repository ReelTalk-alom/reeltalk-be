package com.alom.reeltalkbe.content.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// Dto 는 jackson 의 역직렬화 위해 Setter 나 생성자 필요
public class RatingRequest {
    // todo : @NotNull , @Valid, bindingResult
    private int rating;
}
