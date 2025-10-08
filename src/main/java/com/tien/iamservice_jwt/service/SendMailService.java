package com.tien.iamservice_jwt.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.tien.iamservice_jwt.config.SendGridProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j(topic = "email_service")
public class SendMailService {
    private final SendGrid sendGrid;
    private final SendGridProperties senGridProperties;
    public void send(String to, String subject, String text) {
        //email gui tu ai //cau hinh san 1 email (quangtien18137@gmail.com)
        Email fromEmail = new Email(senGridProperties.getFromEmail());
        //gui den ai
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(fromEmail, subject, toEmail, content); //tao ra mail de gui mail
        Request request = new Request(); //tao request de yeu cau gui mail
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);//cam api key-dong vai tro giong token de xac thuc va request truc tiep ve thang sendgrid
            if(response.getStatusCode() == 202) { //gui mail thanh cong(Accepted)
                log.info("Email sent successfully");
            }
            else log.info("Email sent failed");
        } catch (IOException e) {
            log.info("mac loi request, response email");
        }
    }
}
