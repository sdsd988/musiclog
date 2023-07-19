package com.musiclog.domain;

import lombok.Builder;
import lombok.Getter;

//수정 사항을 요청 받는 클래스를 따로 분리하는 이유 1. 확장성 2. 명확성

@Getter
public class PostEditor {

    private String title;
    private  String content ;

    public static PostEditorBuilder builder() {
        return new PostEditorBuilder();
    }

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditorBuilder title(final String title) {
            if(title != null) {
                this.title = title;
            }
            return this;
        }

        public PostEditorBuilder content(final String content) {
            if(content != null) {
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}
