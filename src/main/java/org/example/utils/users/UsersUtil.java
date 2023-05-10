package org.example.utils.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.UserDAO;
import org.example.models.User;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UsersUtil {

    private final UserDAO userDAO;

    public UsersUtil(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getCurrentUser(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("user"))
                .map(Cookie::getValue)
                .map(id -> userDAO.read(Integer.parseInt(id)))
                .findAny()
                .orElse(null);
    }

    public void loginCurrentUser(int id, HttpServletResponse response) {
        Cookie cookie = new Cookie("user", String.valueOf(id));
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void logOutCurrentUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
