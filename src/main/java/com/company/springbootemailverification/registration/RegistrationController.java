package com.company.springbootemailverification.registration;

import com.company.springbootemailverification.entity.User;
import com.company.springbootemailverification.event.RegistrationCompleteEvent;
import com.company.springbootemailverification.registration.RegisterRequest;
import com.company.springbootemailverification.registration.token.VerificationToken;
import com.company.springbootemailverification.registration.token.VerificationTokenRepository;
import com.company.springbootemailverification.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository verificationTokenRepository;

    @PostMapping
    public String registerUser(@RequestBody RegisterRequest registerRequest, final HttpServletRequest request) {
        User user = userService.registerUser(registerRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success! Please , Check your email to complete the registration process";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = verificationTokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()) {
            return "This account has already been verify , please login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")) {
            return "Email verified successfully . Now you can loin to account";
        }
        return "Invalid verification token";
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
