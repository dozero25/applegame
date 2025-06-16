package com.project.dozeo_appleGame.repository.account;

import com.project.dozeo_appleGame.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<User, Long>, AccountRepositoryCustom {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);

    User findUserByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Long findUserIdByUsername(String username);

    @Query("SELECT u.nickname FROM User u WHERE u.username = :username")
    String findNicknameByUsername(String username);

    User findByEmail(String email);

}
