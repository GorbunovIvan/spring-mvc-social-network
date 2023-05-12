package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.PostDAO;
import org.example.models.Post;
import org.example.models.User;
import org.example.utils.users.Error;
import org.example.utils.users.UsersUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostDAO postDAO;
    private final UsersUtil usersUtil;

    public PostsController(PostDAO postDAO, UsersUtil usersUtil) {
        this.postDAO = postDAO;
        this.usersUtil = usersUtil;
    }

    @PostMapping
    public String create(@ModelAttribute Post post, HttpServletRequest request) {

        User user = usersUtil.getCurrentUser(request);
        if (user == null) {
            var error = new Error(401, "You are not authorized", "");
            return error.getRedirectPath();
        }

        post.setAuthor(user);

        if (post.getContent() == null
            || post.getContent().length() < 1)
            return "redirect:/users/" + post.getAuthor().getId();

        postDAO.create(post);

        return "redirect:/users/" + post.getAuthor().getId();
    }
}
