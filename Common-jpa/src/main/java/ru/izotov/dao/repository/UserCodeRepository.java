package ru.izotov.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.izotov.entity.UserCode;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {

    @Transactional
    @Query("select uc from UserCode uc where uc.appUser.id = :id and uc.status = 'ACTIVE'")
    UserCode findActiveUserCodeByUserId(@Param(value = "id") Long userId);
}
