package com.musiclog.request.post;

import com.musiclog.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요!") //  공백과 null 체크도 해준다, 메세지 수정
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요!")
    private String content;

    public PostCreate() {
    }

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }





    //빌더의 장점
    // -가독성에 좋다.(값 생성의 유연함)
    // - 필요한 값만 받을 수 있다. //->(오버로딩 가능한 조건 찾아볼 것)
    // - 객체의 불변성
}
