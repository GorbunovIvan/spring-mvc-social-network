package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.FriendsDAO;
import org.example.dao.UserDAO;
import org.example.models.User;
import org.example.utils.users.UsersUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("{id}/friends")
public class FriendsController {

    private final FriendsDAO friendsDAO;
    private final UserDAO userDAO;
    private final UsersUtil usersUtil;

    public FriendsController(FriendsDAO friendsDAO, UserDAO userDAO, UsersUtil usersUtil) {
        this.friendsDAO = friendsDAO;
        this.userDAO = userDAO;
        this.usersUtil = usersUtil;
    }

    @GetMapping
    public String friends(@PathVariable int id, Model model, HttpServletRequest request) {

        User user = userDAO.readUserWithFriends(id);
        User currentUser = usersUtil.getCurrentUser(request);

        boolean isCurrentUser = currentUser != null && currentUser.getId().equals(id);

        model.addAttribute("user", user);
        model.addAttribute("isCurrentUser", isCurrentUser);

        return "friends/friends";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int friendId, HttpServletRequest request) {

        User currentUser = usersUtil.getCurrentUser(request);
        User friend = userDAO.read(friendId);

        if (currentUser == null)
            throw new IllegalStateException("Unauthorized user");

        friendsDAO.delete(currentUser, friend);

        return "redirect:/friends/" + currentUser.getId();
    }

}
