package com.my.bob.core.domain.base.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ConditionalSerializer extends JsonSerializer<String> {
    // swagger 문서 직렬화를 위한 값
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // 직렬화 대상 객체의 부모를 가져오기
        Object parent = gen.getCurrentValue();
        if(parent instanceof ResponseDto<?> responseDto && responseDto.isFail()) {
            // status가 FAIL일 경우에만 필드 출력
            gen.writeString(value);
        }
    }
}
