package com.inscription.app.security.oauth2;

import com.inscription.app.security.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.inscription.app.InscriptionApp.log;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    public OAuth2SuccessHandler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println("✅ OAuth2SuccessHandler déclenché !");
        log.info("✅ OAuth2SuccessHandler déclenché !");

        // 👉 ICI tu ajoutes le log utile pour voir l'identité de l'utilisateur
        System.out.println("✅ Azure/Google succès avec utilisateur = " + authentication.getName());

        String jwt = tokenProvider.createToken(authentication, false);
        log.debug(">>> Succès OAuth2. JWT = {}", jwt);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{ \"token\": \"" + jwt + "\" }");
        response.sendRedirect("http://localhost:9000/?jwt=" + jwt);
        // temporairement pour test
        response.getWriter().write("OAuth2 Login success. JWT: " + jwt);
    }

}
