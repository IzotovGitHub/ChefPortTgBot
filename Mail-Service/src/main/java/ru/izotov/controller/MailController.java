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
            mailService.send("chef-port@bot.com", requestBody.get("email").asText(), "Активация учетной записи", "Тут что то важное =)");
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            log.error("", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
