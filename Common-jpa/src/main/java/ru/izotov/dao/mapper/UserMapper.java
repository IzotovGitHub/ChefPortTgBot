package ru.izotov.dao.mapper;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.izotov.entity.AppUser;

import static ru.izotov.enums.UserStatus.ACTIVE;

@Service
public class UserMapper {

    public AppUser toAppUser(User user) {
        return AppUser.builder()
                .tgUserId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .isActive(true)
                .status(ACTIVE)
                .build();
    }
}
