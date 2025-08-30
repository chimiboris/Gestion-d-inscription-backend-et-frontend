package com.inscription.app.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String token) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // ✅ CORRECTION : utiliser MultiValueMap au lieu de Map
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", recaptchaSecret);
        body.add("response", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, request, Map.class);
            Object success = response.getBody().get("success");
            return success != null && Boolean.TRUE.equals(success);
        } catch (Exception e) {
            System.err.println("Erreur reCAPTCHA : " + e.getMessage());
            return false;
        }
    }
}



//package com.inscription.app.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import java.util.*;
//
//@Service
//public class RecaptchaService {
//
//    @Value("${google.recaptcha.secret}")
//    private String recaptchaSecret;
//
//    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
//
//    public boolean verify(String token) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("secret", recaptchaSecret);
//        body.add("response", token);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, request, Map.class);
//            return (Boolean) response.getBody().get("success");
//        } catch (Exception e) {
//            System.err.println("Échec de la vérification reCAPTCHA : " + e.getMessage());
//            return false;
//        }
//    }

//    public boolean verify(String token) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        Map<String, String> body = new LinkedHashMap<>();
//        body.put("secret", recaptchaSecret);
//        body.put("response", token);
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, request, Map.class);
//            return (Boolean) response.getBody().get("success");
//        } catch (Exception e) {
//            System.err.println("Échec de la vérification reCAPTCHA : " + e.getMessage());
//            return false;
//        }
//    }
//}
