package com.restapi.halliburton.service;

import com.restapi.halliburton.dto.CreateUserRequest;
import com.restapi.halliburton.dto.UserPatchRequest;
import com.restapi.halliburton.dto.UserResponse;
import com.restapi.halliburton.entity.UserEntity;
import com.restapi.halliburton.exception.UserNotFoundException;
import com.restapi.halliburton.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_returnsUser() {
        UserEntity entity = new UserEntity("Rajesh");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        UserResponse result = userService.findById(1L);

        assertThat(result.name()).isEqualTo("Rajesh");
    }

    @Test
    void findById_whenMissing_throwsNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_savesEntityWithGivenFields() {
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.create(new CreateUserRequest("Rajesh", "r@example.com"));

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Rajesh");
        assertThat(captor.getValue().getEmail()).isEqualTo("r@example.com");
    }

    @Test
    void patch_updatesOnlyProvidedFields() {
        UserEntity entity = new UserEntity("Old Name");
        entity.setEmail("old@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        userService.patch(1L, new UserPatchRequest(null, "new@example.com"));

        assertThat(entity.getName()).isEqualTo("Old Name");
        assertThat(entity.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    void patch_ignoresBlankValues() {
        UserEntity entity = new UserEntity("Old Name");
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        userService.patch(1L, new UserPatchRequest("  ", ""));

        assertThat(entity.getName()).isEqualTo("Old Name");
    }

    @Test
    void delete_whenMissing_throwsAndDeletesNothing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(UserNotFoundException.class);
        verify(userRepository, never()).delete(any());
    }
}