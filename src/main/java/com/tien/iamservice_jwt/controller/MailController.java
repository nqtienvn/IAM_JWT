package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.service.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "email_controller")
public class MailController {
    private final SendMailService sendMailService;
    @GetMapping("/send-mail")
    public void send(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        log.info("Sending email to {}", to);
        sendMailService.send(to, subject, content);
        log.info("Email sent successfully");
    }
}
