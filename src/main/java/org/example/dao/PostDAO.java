package org.example.dao;

import org.example.models.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostDAO extends BaseDAO<Post> {

    public PostDAO() {
        super(Post.class);
    }

    @Override
    public void create(Post post) {
        if (post.getTime() == null || post.getTime().getYear() == 1)
            post.setTime(LocalDateTime.now());
        super.create(post);
    }
}
