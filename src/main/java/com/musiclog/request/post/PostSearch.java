package com.musiclog.request.post;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final  int max_size = 2000;
    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;


    public long getOffset(){
        return (long) (max(1,page) - 1) * min(size,max_size);
    }
}
