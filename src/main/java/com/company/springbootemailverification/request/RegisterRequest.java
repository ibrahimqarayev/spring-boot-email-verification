package com.company.springbootemailverification.request;

public record RegisterRequest(
        String firstname,
        String lastname,
        String email,
        String password
) {
}
