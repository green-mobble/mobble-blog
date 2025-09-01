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

    public Optional<User> findByUsername(String username) {
        Query query = em.createQuery("select u from User u where u.username=:username",User.class);
        query.setParameter("username",username);
        return Optional.ofNullable((User) query.getSingleResult());
    }

    //저장
    public User save(User user) {
        em.persist(user);
        return user;
    }

    public Optional<User> findById(Integer userId) {
        return  Optional.ofNullable(em.find(User.class, userId));
    }
}
