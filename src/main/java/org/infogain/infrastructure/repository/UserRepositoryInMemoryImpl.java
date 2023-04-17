package org.infogain.infrastructure.repository;

import org.infogain.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {

    @Value("users")
    private List<String> userIds;

    @Override
    public boolean userExists(String userId) {
        return userIds.contains(userId);
    }
}
