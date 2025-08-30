package com.inscription.app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaValidator {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean isCaptchaValid(String token) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> body = Map.of(
            "secret", recaptchaSecret,
            "response", token
        );

        try {
            Map response = restTemplate.postForObject(VERIFY_URL + "?secret={secret}&response={response}", null, Map.class, body);
            return Boolean.TRUE.equals(response.get("success"));
        } catch (Exception e) {
            return false;
        }
    }
}
