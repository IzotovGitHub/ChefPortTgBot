package ru.izotov.dao.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.izotov.dao.repository.AppUserRepository;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.entity.AppUser;
import ru.izotov.enums.UserStatus;

import java.util.Optional;

import static java.lang.String.format;

@Log4j
@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    @Override
    public Optional<AppUser> findAppUserByTelegramId(Long telegramUserId) {
        try {
            return repository.findByTgUserId(telegramUserId);
        } catch (Exception e) {
            log.error(format("Unexpected error when trying to find user by telegram user id. Telegram user id: '%s'", telegramUserId), e);
            return Optional.empty();
        }
    }

    @Override
    public AppUser create(AppUser appUser) {
        try {
            return repository.saveAndFlush(appUser);
        } catch (Exception e) {
            log.error("Unexpected error when trying to create user", e);
            return appUser;
        }
    }

    @Override
    public AppUser update(AppUser user) {
        try {
            return repository.saveAndFlush(user);
        } catch (Exception e) {
            log.error("Unexpected error when trying to create user", e);
            return user;
        }
    }

    @Override
    public boolean isEmailAlreadyInUse(String email) {
        try {
            return repository.findByEmail(email).isPresent();
        } catch (Exception e) {
            log.error("Unexpected error when trying to get user by email", e);
            return true;
        }
    }

    @Override
    public void updateStatus(AppUser appUser, UserStatus status) {
        try {
            if (appUser.getStatus() != status) {
                appUser.setStatus(status);
                repository.saveAndFlush(appUser);
            }
        } catch (Exception e) {
            log.error("Unexpected error when trying to update user status", e);
        }
    }
}
