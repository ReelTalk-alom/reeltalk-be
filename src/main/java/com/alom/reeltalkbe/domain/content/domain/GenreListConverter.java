package com.alom.reeltalkbe.domain.content.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.List;

@Converter
public class GenreListConverter implements AttributeConverter<List<Genre>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Genre> genreList) {
        if (genreList == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(genreList);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting list of genres to JSON", e);
        }
    }

    @Override
    public List<Genre> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Genre>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to list of genres", e);
        }
    }
}
