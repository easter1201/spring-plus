package org.example.expert.domain.user;

import org.example.expert.config.JwtAuthenticationFilter;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class UserSearchTest {
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void user_search_correctly(){
        //given
        String targetNickName = "target";
        long start = System.currentTimeMillis();
        //when
        List<User> found = userRepository.findByNickName(targetNickName);
        long end = System.currentTimeMillis();
        //then
        System.out.println("소요시간(ms): " + (end - start));
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getNickName()).isEqualTo(targetNickName);
    }
}
