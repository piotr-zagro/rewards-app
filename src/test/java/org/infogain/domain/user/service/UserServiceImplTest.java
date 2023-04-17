package org.infogain.domain.user.service;

import org.infogain.domain.user.exception.UserNotFoundException;
import org.infogain.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String USER_ID = "userId";

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void should_validateUserExists_returnNothing_whenUserIdExists() {
        // given
        when(userRepository.userExists(USER_ID)).thenReturn(true);

        // when // then
        assertDoesNotThrow(() -> userService.validateUserExists(USER_ID));
    }

    @Test
    void should_validateUserExists_throwException_whenUserIdNotFound() {
        // given
        when(userRepository.userExists(USER_ID)).thenReturn(false);

        // when // then
        assertThrows(UserNotFoundException.class, () -> userService.validateUserExists(USER_ID));
    }
}