package org.example.dao;

import org.example.models.Friends;
import org.example.models.User;
import org.springframework.stereotype.Component;

@Component
public class FriendsDAO extends BaseDAO<Friends> {

    public FriendsDAO() {
        super(Friends.class);
    }

    public void delete(User friend1, User friend2) {

        Friends friendsRelation = doInSessionAndReturn(entityManager -> {
            return entityManager.createQuery("FROM Friends " +
                    "WHERE (inviter=:friend1 AND receiver=:friend2) " +
                    "OR (inviter=:friend2 AND receiver=:friend1)", Friends.class)
                    .setParameter("friend1", friend1)
                    .setParameter("friend2", friend2)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
        });

        if (friendsRelation == null)
            throw new IllegalArgumentException("These two users have no relations");

        doInSession(entityManager -> {
            entityManager.createQuery("DELETE FROM Friends " +
                            "WHERE (inviter=:inviter AND receiver=:receiver)", Friends.class)
                    .setParameter("inviter", friendsRelation.getInviter())
                    .setParameter("receiver", friendsRelation.getReceiver())
                    .executeUpdate();
        });
    }
}
