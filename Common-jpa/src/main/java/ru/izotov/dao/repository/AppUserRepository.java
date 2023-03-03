package ru.izotov.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.izotov.entity.AppUser;
import ru.izotov.enums.UserStatus;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @Modifying
    @Transactional
    @Query("update AppUser a set a.status = :status where a.id = :id")
    void updateStatusById(@Param(value = "id") Long userId, @Param(value = "status") UserStatus status);

    Optional<AppUser> findByTgUserId(Long tgUserId);

    Optional<AppUser> findByEmail(String email);
}
