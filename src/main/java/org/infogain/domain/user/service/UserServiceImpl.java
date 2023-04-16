package org.infogain.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.infogain.domain.user.exception.UserNotFoundException;
import org.infogain.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void validateUserExists(String userId) {
        if (!userRepository.userExists(userId)) {
            throw new UserNotFoundException(userId);
        }
    }
}
