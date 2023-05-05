package com.company.springbootemailverification.event.listeneer;

import com.company.springbootemailverification.entity.User;
import com.company.springbootemailverification.event.RegistrationCompleteEvent;
import com.company.springbootemailverification.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private User theUser;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        theUser = event.getUser();

        String verificationToken = UUID.randomUUID().toString();

        userService.saveUserVerificationToken(theUser, verificationToken);

        String url = event.getApplicationUrl() + "/register/verify-email?token=" + verificationToken;

        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("Click the link to verify your registration : {}", url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal";
        String mailContent = "<p> Hi," + theUser.getFirstname() + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify email to activate your account</a>" +
                "<p>Thank you <br> Users Registration Portal";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("mailformyapplication@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }

}
