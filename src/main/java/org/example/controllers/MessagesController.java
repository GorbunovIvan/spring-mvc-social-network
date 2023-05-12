package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.dao.MessageDAO;
import org.example.dao.UserDAO;
import org.example.models.Message;
import org.example.models.User;
import org.example.utils.users.Error;
import org.example.utils.users.UsersUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessagesController {

    private final MessageDAO messageDAO;
    private final UserDAO userDAO;
    private final UsersUtil usersUtil;

    public MessagesController(UserDAO userDAO, MessageDAO messageDAO, UsersUtil usersUtil) {
        this.messageDAO = messageDAO;
        this.userDAO = userDAO;
        this.usersUtil = usersUtil;
    }

    @GetMapping
    public String messages(Model model, HttpServletRequest request) {

        User currentUser = usersUtil.getCurrentUser(request);

        if (currentUser == null) {
            var error = new Error(401, "You are not authorized", "");
            return error.getRedirectPath();
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("interlocutors", messageDAO.readInterlocutorsByUser(currentUser.getId()));

        return "/messages/messages";
    }

    @GetMapping("/{userId}")
    public String chat(@PathVariable int userId, Model model, HttpServletRequest request) {

        User currentUser = usersUtil.getCurrentUser(request);
        User user = userDAO.read(userId);

        if (currentUser == null) {
            var error = new Error(401, "You are not authorized", "");
            return error.getRedirectPath();
        }

        if (user == null) {
            var error = new Error(404, "No user with id '" + userId + "' is found", "");
            return error.getRedirectPath();
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("user", user);
        model.addAttribute("messages", messageDAO.readMessages(currentUser.getId(), userId));
        model.addAttribute("newMessage", new Message(currentUser, user));

        return "/messages/chat";
    }

    @PostMapping("/{userId}")
    public String create(@PathVariable int userId,
                         @ModelAttribute @Valid Message message, BindingResult bindingResult,
                         HttpServletRequest request) {

        if (bindingResult.hasFieldErrors("text"))
            return "redirect:/messages/" + userId;

        User currentUser = usersUtil.getCurrentUser(request);
        User user = userDAO.read(userId);

        if (message.getAuthor() == null)
            message.setAuthor(currentUser);
        if (message.getReceiver() == null)
            message.setReceiver(user);

        messageDAO.create(message);

        return "redirect:/messages/" + userId;
    }
}
