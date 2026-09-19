package com.restapi.halliburton.service;

import com.restapi.halliburton.dto.UserPatchRequest;
import com.restapi.halliburton.dto.UserResponse;
import com.restapi.halliburton.entity.UserEntity;
import com.restapi.halliburton.exception.UserNotFoundException;
import com.restapi.halliburton.repository.UserRepository;
import com.restapi.halliburton.dto.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(getOrThrow(id));
    }

    @Transactional
    public UserResponse create(@Valid CreateUserRequest request) {
        UserEntity user = new UserEntity(request.name());
        user.setEmail(request.email());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse patch(Long id, @Valid UserPatchRequest request) {
        UserEntity user = getOrThrow(id);

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }
        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email());
        }
        return UserResponse.from(user); // saved automatically at transaction commit
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(getOrThrow(id));
    }

    private UserEntity getOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
