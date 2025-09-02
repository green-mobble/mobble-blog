package org.example.mobble.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final EntityManager em;

    public User findUsername(String username) {
        // try - catch => Optional로 처리
        try {
            Query query = em.createQuery("select u from User u where u.username=:username");
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch(RuntimeException e) {
            return null;
        }

    }
    public void userSave(User user) {
        em.persist(user);
    }

}
