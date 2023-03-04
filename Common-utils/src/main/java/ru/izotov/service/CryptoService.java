package ru.izotov.service;

public interface CryptoService {

    String hashOf(Long id);

    Long idOf(String hash);
}
