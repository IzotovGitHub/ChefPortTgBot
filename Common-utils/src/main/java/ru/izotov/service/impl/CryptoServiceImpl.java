package ru.izotov.service.impl;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.izotov.service.CryptoService;

import javax.annotation.PostConstruct;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Value("${min.hash.length}")
    private Integer minHashLength;

    @Value("${salt}")
    private String salt;
    private Hashids hashids;

    @PostConstruct
    void init() {
        this.hashids = new Hashids(salt, minHashLength);
    }

    @Override
    public String hashOf(Long id) {
        return hashids.encode(id);
    }

    @Override
    public Long idOf(String hash) {
        long[] res = hashids.decode(hash);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
