package org.example.utils.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.UserDAO;
import org.example.models.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UsersUtil {

    private final UserDAO userDAO;
    private User currentUser;

    public UsersUtil(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getCurrentUser(HttpServletRequest request) {
        return getCurrentUser(request, false);
    }

    public User getCurrentUser(HttpServletRequest request, boolean getWithActualProperties) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null)
            return null;

        Integer id = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("user"))
                .map(Cookie::getValue)
                .map(Integer::parseInt)
                .findAny()
                .orElse(null);

        if (id == null)
            return null;

        if (currentUser != null && !getWithActualProperties) {
            if (currentUser.getId().equals(id))
                return currentUser;
        }

        currentUser = userDAO.read(id);

        return currentUser;
    }

    public void loginCurrentUser(int id, HttpServletResponse response) {

        currentUser = userDAO.read(id);

        if (currentUser == null)
            throw new IllegalArgumentException("No user with such id (" + id + ") is found");

        Cookie cookie = new Cookie("user", String.valueOf(id));
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void logOutCurrentUser(HttpServletResponse response) {

        currentUser = null;

        Cookie cookie = new Cookie("user", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
