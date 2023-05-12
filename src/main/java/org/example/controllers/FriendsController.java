package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.dao.FriendsDAO;
import org.example.dao.UserDAO;
import org.example.models.Friends;
import org.example.models.User;
import org.example.utils.users.Error;
import org.example.utils.users.UsersUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/friends/{userId}")
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
    public String friends(@PathVariable int userId, Model model, HttpServletRequest request) {

        User user = userDAO.readUserWithFriends(userId);
        User currentUser = usersUtil.getCurrentUser(request);

        boolean isCurrentUser = currentUser != null && currentUser.getId().equals(userId);

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isCurrentUser", isCurrentUser);

        return "friends/friends";
    }

    @PostMapping("/{friendId}")
    public String create(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId,
                         HttpServletRequest request) {

        User currentUser = usersUtil.getCurrentUser(request);

        if (currentUser == null
            || !currentUser.getId().equals(userId)) {
            var error = new Error(403, "You are not allowed to do this", "");
            return error.getRedirectPath();
        }

        User friend = userDAO.read(friendId);

        var friendsRelationship = new Friends();
        friendsRelationship.setInviter(currentUser);
        friendsRelationship.setReceiver(friend);
        friendsRelationship.setTime(LocalDateTime.now());

        friendsDAO.create(friendsRelationship);

        return "redirect:/users/" + friend.getId();
    }

    @DeleteMapping("/{friendId}")
    public String delete(@PathVariable("userId") int userId,
                         @PathVariable("friendId") int friendId,
                         HttpServletRequest request) {

        User currentUser = usersUtil.getCurrentUser(request);
        User friend = userDAO.read(friendId);

        if (currentUser == null) {
            var error = new Error(401, "You are not authorized", "");
            return error.getRedirectPath();
        }

        if (!currentUser.getId().equals(userId)) {
            var error = new Error(403, "You are not allowed to do this", "");
            return error.getRedirectPath();
        }

        friendsDAO.delete(currentUser, friend);

        return "redirect:/friends/" + currentUser.getId();
    }

}
