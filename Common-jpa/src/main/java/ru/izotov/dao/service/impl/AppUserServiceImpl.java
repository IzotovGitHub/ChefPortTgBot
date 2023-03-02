package ru.izotov.dao.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.izotov.dao.repository.AppUserRepository;
import ru.izotov.dao.service.AppUserService;
import ru.izotov.entity.AppUser;
import ru.izotov.enums.UserStatus;

import static java.lang.String.format;

@Log4j
@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    @Override
    public AppUser findAppUserByTelegramId(Long telegramUserId) {
        try {
            return repository.findByTgUserId(telegramUserId);
        } catch (Exception e) {
            log.error(format("Unexpected error when trying to find user by telegram user id. Telegram user id: '%s'", telegramUserId), e);
            return null;
        }
    }

    @Override
    public AppUser create(AppUser appUser) {
        try {
            return repository.save(appUser);
        } catch (Exception e) {
            log.error("Unexpected error when trying to create user", e);
            return null;
        }
    }

    public void updateStatus(AppUser appUser, UserStatus status) {
        try {
            if (appUser.getStatus() != status) {
                repository.updateStatusById(appUser.getId(), status);
            }
        } catch (Exception e) {
            log.error("Unexpected error when trying to update user status", e);
        }
    }
}
