package ru.izotov.dao.service;


import ru.izotov.entity.AppUser;
import ru.izotov.enums.UserStatus;

import java.util.Optional;

public interface AppUserService {

    /**
     * Method for searching bot users.
     * Returns {@code null} if the user is not found or exception an occurred.
     *
     * @param telegramUserId Telegram user ID
     * @return The {@code AppUser} instance, or null if it doesn't exist or exception an occurred
     */
    Optional<AppUser> findAppUserByTelegramId(Long telegramUserId);

    /**
     * Method for saving a new user
     * Returns {@code null} if exception an occurred.
     *
     * @param appUser user to be created
     * @return The {@code AppUser} instance, or null if exception an occurred
     */
    AppUser create(AppUser appUser);

    AppUser update(AppUser user);

    boolean isEmailAlreadyInUse(String email);

    void updateStatus(AppUser appUser, UserStatus status);
}
