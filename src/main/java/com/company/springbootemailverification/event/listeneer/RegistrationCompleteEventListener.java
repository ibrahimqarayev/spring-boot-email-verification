package com.company.springbootemailverification.event.listeneer;

import com.company.springbootemailverification.entity.User;
import com.company.springbootemailverification.event.RegistrationCompleteEvent;
import com.company.springbootemailverification.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService ;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User theUser = event.getUser();

        String verificationToken = UUID.randomUUID().toString();

        userService.saveUserVerificationToken(theUser,verificationToken);

        String url = event.getApplicationUrl() + "/register/verify-email?token=" + verificationToken;

        log.info("Click the link to verify your registration : {}",url);
    }
}
