package ru.izotov.srvice;

public interface MailService {

    void send(String emailFrom, String emailTo, String subject, String body);
}
