package com.musiclog.repository.post;

import com.musiclog.domain.Post;
import com.musiclog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
