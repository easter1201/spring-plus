package org.example.expert.domain.user;

import jakarta.persistence.EntityManager;
import org.example.expert.config.JwtAuthenticationFilter;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class UserGeneratorTest {
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @BeforeEach
    void clearDatabase(){userRepository.deleteAll();}


    @Test
    @Transactional
    @Rollback(false)
    public void generateUsers(){
        //given
        int totalUsers = 1_000_000;
        List<User> users = new ArrayList<>(1000);

        for(int i = 0; i < totalUsers; i++){
            String nickname = "user_" + UUID.randomUUID().toString().substring(0, 8);
            User user = new User("user" + i + "@email.com", "1q2w3e4R!", UserRole.USER, nickname);
            users.add(user);

            if(users.size() == 1000){
                userRepository.saveAll(users);
                entityManager.clear();
                users.clear();
                System.out.println(i + " users inserted!");
            }
        }
        if(!users.isEmpty()){
            userRepository.saveAll(users);
        }

        //when
        long count = userRepository.count();

        //then
        assertThat(count).isEqualTo(1_000_000L);
    }
}
