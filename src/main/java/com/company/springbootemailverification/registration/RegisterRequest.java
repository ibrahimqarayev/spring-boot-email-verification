package com.company.springbootemailverification.registration;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
