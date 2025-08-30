package com.inscription.app.security.oauth2;

import com.inscription.app.domain.Authority;
import com.inscription.app.domain.User;
import com.inscription.app.repository.AuthorityRepository;
import com.inscription.app.repository.UserRepository;
import com.inscription.app.security.AuthoritiesConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

import java.util.*;
import java.util.stream.Collectors;

import static com.inscription.app.InscriptionApp.log;

@Service
public class AzureOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public AzureOidcUserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getEmail(); // ou bien oidcUser.getAttribute("email");

        System.out.println("===== OIDC ATTRIBUTES =====");
        oidcUser.getAttributes().forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println("===========================");

        //String email = oidcUser.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found in Azure response");
        }

        log.info("Authenticating Azure user: {}", email);

        // Rechercher ou créer l'utilisateur
        User user = userRepository.findOneByEmailIgnoreCase(email).orElseGet(() -> {
            log.info("Creating new user from Azure account: {}", email);
            User newUser = new User();
            newUser.setLogin(email);
            newUser.setEmail(email);
            newUser.setFirstName((String) oidcUser.getAttribute("given_name"));
            newUser.setLastName((String) oidcUser.getAttribute("family_name"));
            newUser.setLangKey("fr");
            newUser.setActivated(true);

            Authority authority = authorityRepository.findById(AuthoritiesConstants.USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database"));
            newUser.setAuthorities(Set.of(authority));

            return userRepository.save(newUser);
        });

        List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
            .map(auth -> new SimpleGrantedAuthority(auth.getName()))
            .collect(Collectors.toList());

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
    }

//    @Override
//    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
//        OidcUser oidcUser = super.loadUser(userRequest);
//
//        String email = oidcUser.getEmail(); // ou bien oidcUser.getAttribute("email");
//
//        System.out.println("===== OIDC ATTRIBUTES =====");
//        oidcUser.getAttributes().forEach((key, value) -> System.out.println(key + " : " + value));
//        System.out.println("===========================");
//
//        Optional<User> userOptional = userRepository.findOneByEmailIgnoreCase(email);
//
//        User user;
//        if (userOptional.isPresent()) {
//            user = userOptional.get();
//        } else {
//            user = new User();
//            user.setLogin(email);
//            user.setEmail(email);
//            user.setFirstName(oidcUser.getGivenName()); // peut être null
//            user.setLastName(oidcUser.getFamilyName()); // peut être null
//            user.setActivated(true);
//            user.setLangKey("en");
//            user.setCreatedBy("oidc");
//
//            // ROLE par défaut
//            Authority authority = authorityRepository.findById("ROLE_USER")
//                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database"));
//            user.setAuthorities(Set.of(authority));
//
//            userRepository.save(user);
//        }
//
//        return oidcUser;
//    }


//    @Override
//    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
//        OidcUser oidcUser = super.loadUser(userRequest);
//
//        System.out.println("===== AZURE ATTRIBUTES =====");
//        oidcUser.getAttributes().forEach((key, value) ->
//            System.out.println(key + " : " + value));
//        System.out.println("============================");
//
//        return oidcUser;
//    }
}

