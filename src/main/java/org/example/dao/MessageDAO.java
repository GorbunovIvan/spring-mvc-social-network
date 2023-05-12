package org.example.dao;

import org.example.models.Message;
import org.example.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MessageDAO extends BaseDAO<Message> {

    public MessageDAO() {
        super(Message.class);
    }

    public Set<User> readInterlocutorsByUser(Integer userId) {
        return doInSessionAndReturn(entityManager -> {

            Set<User> users1 = entityManager.createQuery("SELECT DISTINCT " +
                            "user " +
                            "FROM User user " +
                            "JOIN FETCH user.messagesIReceived messages " +
                            "WHERE messages.author.id=:userId", User.class)
                    .setParameter("userId", userId)
                    .getResultStream()
                    .collect(Collectors.toSet());

            Set<User> users2 = entityManager.createQuery("SELECT DISTINCT " +
                            "user " +
                            "FROM User user " +
                            "JOIN FETCH user.messagesIWrote messages " +
                            "WHERE messages.receiver.id=:userId", User.class)
                    .setParameter("userId", userId)
                    .getResultStream()
                    .collect(Collectors.toSet());

            users1.addAll(users2);

            return users1;
        });
    }

    public Set<Message> readMessages(Integer userId1, Integer userId2) {
        return doInSessionAndReturn(entityManager -> {
           return entityManager.createQuery("FROM Message " +
                            "WHERE (author.id=:userId1 AND receiver.id=:userId2)" +
                            "   OR (author.id=:userId2 AND receiver.id=:userId1)" +
                            "ORDER BY time", Message.class)
                   .setParameter("userId1", userId1)
                   .setParameter("userId2", userId2)
                   .getResultStream()
                   .collect(Collectors.toSet());
        });
    }

    @Override
    public void create(Message message) {
        if (message.getTime() == null || message.getTime().getYear() == 1)
            message.setTime(LocalDateTime.now());
        super.create(message);
    }
}
