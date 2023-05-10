package org.example.utils.users;

import jakarta.servlet.http.HttpServletRequest;
import org.example.models.Post;
import org.example.models.User;
import org.springframework.stereotype.Component;

@Component
public class PostsUtil {

    private final UsersUtil usersUtil;

    public PostsUtil(UsersUtil usersUtil) {
        this.usersUtil = usersUtil;
    }

    public Post getNewPost(HttpServletRequest request) {

        User user = usersUtil.getCurrentUser(request);

        if (user == null)
            throw new IllegalStateException("No authorized user");

        Post post = new Post();
        post.setAuthor(user);

        return post;
    }
}
