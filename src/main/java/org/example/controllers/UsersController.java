package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.dao.UserDAO;
import org.example.models.Post;
import org.example.models.User;
import org.example.utils.users.PostsUtil;
import org.example.utils.users.UsersUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserDAO userDAO;
    private final UsersUtil usersUtil;
    private final PostsUtil postsUtil;

    public UsersController(UserDAO userDAO, UsersUtil usersUtil, PostsUtil postsUtil) {
        this.userDAO = userDAO;
        this.usersUtil = usersUtil;
        this.postsUtil = postsUtil;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userDAO.readAll());
        return "/users/users";
    }

    @GetMapping("/{id}")
    public String user(@PathVariable Integer id, Model model, HttpServletRequest request) {
        model.addAttribute("user", userDAO.readFull(id));
        model.addAttribute("newPost", new Post());
        return "/users/user";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("user", new User());
        return "/users/new";
    }

    @PostMapping
    public String create(@ModelAttribute @Valid User user, BindingResult bindingResult,
                         HttpServletResponse response) {

        if (bindingResult.hasErrors())
            return "redirect:/users/new";

        User userWithTheSameLogin = userDAO.readByLogin(user.getLogin());
        if (userWithTheSameLogin != null) {
            bindingResult.addError(new ObjectError("login", "login exists"));
            return "redirect:/users/new";
        }

        userDAO.create(user);

        usersUtil.loginCurrentUser(user.getId(), response);

        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("user", userDAO.read(id));
        return "/users/edit";
    }

    @PatchMapping("/{id}")
    public String editForm(@PathVariable int id,
                           @ModelAttribute @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "redirect:/users/" + id + "/edit";

        userDAO.update(id, user);

        return "redirect:/users/" + id;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("params", new LoginPassword());
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginPassword params,
                        HttpServletResponse response) {

        User user = userDAO.readByLoginAndPassword(params.getLogin(), params.getPassword());
        if (user == null)
            return "redirect:/users/login";

        usersUtil.loginCurrentUser(user.getId(), response);

        return "redirect:/users/" + user.getId();
    }
}
