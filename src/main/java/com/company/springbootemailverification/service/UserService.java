package com.company.springbootemailverification.service;

import com.company.springbootemailverification.entity.User;
import com.company.springbootemailverification.enums.Role;
import com.company.springbootemailverification.exception.UserAlreadyExistsException;
import com.company.springbootemailverification.registration.token.VerificationToken;
import com.company.springbootemailverification.registration.token.VerificationTokenRepository;
import com.company.springbootemailverification.repository.UserRepository;
import com.company.springbootemailverification.registration.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final VerificationTokenRepository verificationTokenRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User registerUser(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.email());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + registerRequest.email() + " already exists !");
        }

        User user = new User();
        user.setFirstname(registerRequest.firstname());
        user.setLastname(registerRequest.lastname());
        user.setEmail(registerRequest.email());
        user.setPassword(encoder.encode(registerRequest.password()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found !"));
    }

    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }
}
