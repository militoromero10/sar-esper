package com.esper.cep.components;

import com.esper.cep.records.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(@NonNull Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.to());
        message.setSubject(mail.subject());
        message.setText(mail.text());
        emailSender.send(message);
    }
}