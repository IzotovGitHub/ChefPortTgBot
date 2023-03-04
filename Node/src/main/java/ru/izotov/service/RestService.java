package ru.izotov.service;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public interface RestService {

    ResponseEntity<String> sendRequestToMailService(String serviceUrl, JSONObject body);
}
