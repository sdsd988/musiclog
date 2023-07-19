package com.musiclog.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *     "code":"400",
 *     "message":"잘못된 요청입니다.",
 *     "validation":{
 *         "title":"값을 입력해주세요"
 *     }
 * }
 */
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message,Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

}
