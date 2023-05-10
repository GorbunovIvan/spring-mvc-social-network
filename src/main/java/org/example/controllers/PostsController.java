package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.dao.PostDAO;
import org.example.models.Post;
import org.example.models.User;
import org.example.utils.users.UsersUtil;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostDAO postDAO;

    public PostsController(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    @PostMapping
    public String create(@ModelAttribute @Valid Post post, BindingResult bindingResult,
                         HttpServletRequest request, UsersUtil usersUtil) {

        User user = usersUtil.getCurrentUser(request);
        if (user == null)
            throw new IllegalStateException("No authorized user");

        post.setAuthor(user);

        if (bindingResult.hasErrors())
            return "redirect:/users/" + post.getAuthor().getId();

        postDAO.create(post);

        return "redirect:/users/" + post.getAuthor().getId();
    }
}
