package com.lotto;

public interface IntegrationTestData {

    default String requestBodyLogin() {
        return """
                {
                  "login": "maksim@mail.com",
                  "password": "12345"
                }
                """.trim();
    }

    default String requestBodyRegister() {
        return """
                {
                  "email": "maksim@mail.com",
                  "password": "12345"
                }
                """.trim();
    }
}
