package org.example.mobble.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


@Import(UserRepository.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUsername_test() {
        String username = "love";
        User findUsername = userRepository.findUsername(username);
        if (findUsername == null) {
            System.out.println("유저네임 없음 : " + username);
        }else {
            System.out.println("유저네임 있음 : " + username);
        }
    }


}
