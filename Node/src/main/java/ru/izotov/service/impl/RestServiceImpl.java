package ru.izotov.service.impl;

import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.izotov.service.RestService;

@Log4j
@Service
public class RestServiceImpl implements RestService {
    @Override
    public ResponseEntity<String> sendRequestToMailService(String serviceUrl, JSONObject body) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var request = new HttpEntity<>(body.toString(), headers);
        return restTemplate.exchange(serviceUrl,
                HttpMethod.POST,
                request,
                String.class);
    }
}
