package ru.izotov.dao.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.dao.repository.RawDataRepository;
import ru.izotov.dao.service.RawDataService;
import ru.izotov.entity.RawData;

@Log4j
@Service
@AllArgsConstructor
public class RawDataServiceImpl implements RawDataService {
    private final RawDataRepository repository;

    @Override
    public RawData saveRawData(Update update) {
        RawData rawData = RawData.builder().event(update).build();
        return repository.save(rawData);
    }
}
