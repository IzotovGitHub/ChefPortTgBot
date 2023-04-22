package ru.izotov.dao.service;

import ru.izotov.entity.UserCode;

public interface UserCodeService {

    UserCode create(UserCode userCode);

    UserCode findActiveUserCodeByUserId(Long userId);

    UserCode delete(UserCode userCode);
}
