package ru.izotov.srvice.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.izotov.exception.UnexpectedMailException;
import ru.izotov.srvice.MailService;

@Log4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender sender;

    @Override
    public void send(String emailTo, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(body);
        try {
            sender.send(message);
        } catch (Exception e) {
            throw new UnexpectedMailException("Unexpected error when trying to send email", e);
        }
    }
}
