package org.infogain.infrastructure.repository;

import org.infogain.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {

    private final List<String> userIds;

    public UserRepositoryInMemoryImpl(@Value("#{'${userIds}'.split(',')}") List<String> userIds) {
        this.userIds = userIds;
    }

    @Override
    public boolean userExists(String userId) {
        return userIds.contains(userId);
    }
}
