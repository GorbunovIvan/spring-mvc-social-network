package org.example.dao;

import org.example.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserDAO extends BaseDAO<User> {
    public UserDAO() {
        super(User.class);
    }

    public User readByLogin(String login) {
        return doInSessionAndReturn(entityManager -> {
            return entityManager.createQuery("FROM User WHERE login=:login", User.class)
                    .setParameter("login", login)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
        });
    }

    public User readByLoginAndPassword(String login, String password) {
        return doInSessionAndReturn(entityManager -> {
            return entityManager.createQuery("FROM User WHERE login=:login AND password=:password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
        });
    }
}
