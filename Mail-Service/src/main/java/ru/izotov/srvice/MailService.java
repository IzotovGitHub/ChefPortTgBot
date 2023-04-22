package ru.izotov.srvice;

public interface MailService {

    void send(String emailTo, String subject, String body);
}
