package ru.izotov.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.izotov.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByTgUserId(Long tgUserId);
}
