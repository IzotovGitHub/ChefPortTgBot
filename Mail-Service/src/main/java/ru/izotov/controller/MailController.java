package ru.izotov.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.izotov.exception.UnexpectedMailException;
import ru.izotov.srvice.MailService;

@Log4j
@RequestMapping("/mail")
@AllArgsConstructor
@RestController
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody String body) {
        try {
            JsonNode requestBody = new ObjectMapper().readTree(body);
            mailService.send(requestBody.get("emailTo").asText(), requestBody.get("subject").asText(), requestBody.get("body").asText());
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException | UnexpectedMailException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
