package com.musiclog.repository;

import com.musiclog.domain.Post;
import com.musiclog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
