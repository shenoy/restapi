package com.restapi.halliburton.repository;

import com.restapi.halliburton.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_returnsMatchingUser() {
        UserEntity user = new UserEntity("Rajesh");
        user.setEmail("r@example.com");
        userRepository.save(user);

        assertThat(userRepository.findByEmail("r@example.com")).isPresent();
    }
}
