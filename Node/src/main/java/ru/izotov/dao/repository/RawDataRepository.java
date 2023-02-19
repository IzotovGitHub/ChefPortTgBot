package ru.izotov.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.izotov.entity.RawData;

public interface RawDataRepository extends JpaRepository<RawData, Long> {
}
