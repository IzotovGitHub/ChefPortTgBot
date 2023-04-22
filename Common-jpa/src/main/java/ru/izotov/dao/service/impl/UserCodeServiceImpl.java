package ru.izotov.dao.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ru.izotov.dao.repository.UserCodeRepository;
import ru.izotov.dao.service.UserCodeService;
import ru.izotov.entity.UserCode;
import ru.izotov.enums.UserCodeStatus;

@Log4j
@Service
@AllArgsConstructor
public class UserCodeServiceImpl implements UserCodeService {

    private final UserCodeRepository repository;

    @Override
    public UserCode create(UserCode userCode) {
        try {
            return repository.saveAndFlush(userCode);
        } catch (Exception e) {
            log.error("Unexpected error when trying to create userCode", e);
            return userCode;
        }
    }


    @Override
    public UserCode findActiveUserCodeByUserId(Long userId) {
        try {
            return repository.findActiveUserCodeByUserId(userId);
        } catch (Exception e) {
            log.error("Unexpected error when trying to get userCode by user id", e);
            return null;
        }
    }

    @Override
    public UserCode delete(UserCode userCode) {
        try {
            userCode.setStatus(UserCodeStatus.DELETED);
            return repository.saveAndFlush(userCode);
        } catch (Exception e) {
            log.error("Unexpected error when trying to delete userCode", e);
            return userCode;
        }
    }
}
