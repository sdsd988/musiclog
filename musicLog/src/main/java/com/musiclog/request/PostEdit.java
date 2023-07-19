package com.musiclog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostEdit {


    @NotBlank(message = "타이틀을 입력해주세요!") //  공백과 null 체크도 해준다, 메세지 수정
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요!")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
