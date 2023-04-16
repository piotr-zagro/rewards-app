package org.infogain.domain.user.repository;

public interface UserRepository {
    boolean userExists(String userId);
}
