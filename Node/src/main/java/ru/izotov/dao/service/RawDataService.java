package ru.izotov.dao.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.izotov.entity.RawData;

public interface RawDataService {

    RawData saveRawData(Update update);
}
