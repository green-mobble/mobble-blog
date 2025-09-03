package org.example.mobble.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    public Optional<User> findUsername(String username){
        Optional<User> user = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
        return user;
    }
    public User userSave(User user) {
        em.persist(user);
        return user;
    }

}
