package com.inscription.app.security.oauth2; // Assurez-vous que le package est correct

import com.inscription.app.domain.Authority;
import com.inscription.app.domain.User;
import com.inscription.app.repository.AuthorityRepository;
import com.inscription.app.repository.UserRepository;
import com.inscription.app.security.AuthoritiesConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public CustomOAuth2UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        String email = user.getAttribute("email");
        if (email == null) throw new OAuth2AuthenticationException("Email not found in Google response");

        return processOAuth2User(email, user, "google");
    }

    private OAuth2User processOAuth2User(String email, OAuth2User oAuth2User, String provider) {
        User user = userRepository.findOneByEmailIgnoreCase(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setLogin(email);
            newUser.setEmail(email);
            newUser.setFirstName(oAuth2User.getAttribute("given_name"));
            newUser.setLastName(oAuth2User.getAttribute("family_name"));
            newUser.setActivated(true);
            newUser.setLangKey("fr");
            newUser.setCreatedBy("oauth2");

            // Récupérer ROLE_USER depuis la base
            Authority authority = authorityRepository.findById(AuthoritiesConstants.USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database"));

            newUser.setAuthorities(Set.of(authority));
            return userRepository.save(newUser);
        });

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(auth -> new SimpleGrantedAuthority(auth.getName()))
            .collect(Collectors.toList());

        return new DefaultOAuth2User(grantedAuthorities, oAuth2User.getAttributes(), "email");
    }

//    private OAuth2User processOAuth2User(String email, OAuth2User oAuth2User, String provider) {
//        Optional<User> userOptional = userRepository.findOneByEmailIgnoreCase(email);
//        User user = userOptional.orElseGet(() -> {
//            User newUser = new User();
//            newUser.setLogin(email);
//            newUser.setEmail(email);
//            newUser.setFirstName(oAuth2User.getAttribute("given_name"));
//            newUser.setLastName(oAuth2User.getAttribute("family_name"));
//            newUser.setActivated(true);
//            newUser.setLangKey("fr");
//
//            Authority authority = new Authority();
//            authority.setName(AuthoritiesConstants.USER);
//            newUser.setAuthorities(Set.of(authority));
//            return userRepository.save(newUser);
//        });
//
//        List<GrantedAuthority> authorities = user.getAuthorities().stream()
//            .map(auth -> new SimpleGrantedAuthority(auth.getName()))
//            .collect(Collectors.toList());
//
//        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "email");
//    }
}



//package com.inscription.app.security.oauth2; // Assurez-vous que le package est correct
//
//import com.inscription.app.domain.Authority;
//import com.inscription.app.domain.User;
//import com.inscription.app.repository.UserRepository;
//import com.inscription.app.security.AuthoritiesConstants;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.Set;
//
//
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//    private final UserRepository userRepository;
//
//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    @Transactional
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User user = super.loadUser(userRequest);
//        log.info("📩 Google User Attributes: {}", user.getAttributes());
//
//        String email = user.getAttribute("email");
//        if (email == null) {
//            throw new OAuth2AuthenticationException("Email not found");
//        }
//
//        User localUser = userRepository.findOneByEmailIgnoreCase(email)
//            .orElseGet(() -> {
//                User newUser = new User();
//                newUser.setEmail(email);
//                newUser.setLogin(email);
//                newUser.setActivated(true);
//                newUser.setLangKey("en");
//                Authority authority = new Authority();
//                authority.setName(AuthoritiesConstants.USER);
//                newUser.setAuthorities(Set.of(authority));
//                return userRepository.save(newUser);
//            });
//
//        return new DefaultOAuth2User(
//            localUser.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getName())).toList(),
//            user.getAttributes(),
//            "email"
//        );
//    }
//}




//package com.inscription.app.security.oauth2;
//
//import com.inscription.app.domain.Authority;
//import com.inscription.app.domain.User;
//import com.inscription.app.repository.UserRepository;
//import com.inscription.app.security.AuthoritiesConstants;
//import jakarta.transaction.Transactional;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import tech.jhipster.security.RandomUtil;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import static com.inscription.app.InscriptionApp.log;
//
//
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    private final UserRepository userRepository;
//
//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        log.info("✅ CustomOAuth2UserService bien instancié");
//    }
//
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        log.info("🔁 OAuth2 provider: {}", userRequest.getClientRegistration().getRegistrationId());
//
//        OAuth2User oAuth2User;
//        try {
//            // 🔽 Chargement des attributs depuis le provider (Google, Azure, etc.)
//            oAuth2User = super.loadUser(userRequest);
//            log.debug("🧾 Attributs reçus : {}", oAuth2User.getAttributes());
//            log.info("👤 Attributs reçus : {}", oAuth2User.getAttributes());
//            oAuth2User.getAttributes().forEach((key, value) -> log.info("🔑 Clé: {} = {}", key, value));
//
//            // 🔽 Extraction de l’email ou identifiant
//            String email = oAuth2User.getAttribute("email");
//            if (email == null) {
//                email = oAuth2User.getAttribute("preferred_username");
//            }
//            if (email == null) {
//                email = oAuth2User.getAttribute("sub");
//            }
//            if (email == null) {
//                throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "❌ Aucun identifiant utilisateur trouvé dans les attributs.");
//            }
//
////            String email = null;
////
////            if (userRequest.getClientRegistration().getRegistrationId().equals("azure")) {
////                email = oAuth2User.getAttribute("preferred_username"); // Azure AD
////            } else if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
////                email = oAuth2User.getAttribute("email"); // Google
////            }
////
////            if (email == null) {
////                log.error("❌ Aucun identifiant utilisateur trouvé dans les attributs : {}", oAuth2User.getAttributes());
////                throw new OAuth2AuthenticationException("Email ou identifiant introuvable.");
////            }
//
////            String email = oAuth2User.getAttribute("email");
////            if (email == null) {
////                email = oAuth2User.getAttribute("preferred_username");
////            }
////            if (email == null) {
////                email = oAuth2User.getAttribute("sub"); // Fallback ultime
////            }
////
////            if (email == null || !email.contains("@")) {
////                throw new OAuth2AuthenticationException("❌ Email invalide ou manquant dans les attributs : " + oAuth2User.getAttributes());
////            }
//
//            final String finalEmail = email;
//
//            // 🔽 Rechercher ou créer l’utilisateur
//            Optional<User> userOptional = userRepository.findOneByEmailIgnoreCase(finalEmail);
//            User user = userOptional.orElseGet(() -> {
//                User newUser = new User();
//                newUser.setLogin(finalEmail);
//                newUser.setEmail(finalEmail);
//                newUser.setActivated(true);
//                newUser.setFirstName(oAuth2User.getAttribute("given_name"));
//                newUser.setLastName(oAuth2User.getAttribute("family_name"));
//                newUser.setLangKey("fr");
//                newUser.setPassword(RandomUtil.generatePassword());
//
//                Authority authority = new Authority();
//                authority.setName(AuthoritiesConstants.USER);
//                newUser.setAuthorities(Set.of(authority));
//
//                return userRepository.save(newUser);
//            });
//
//            // 🔽 Création de l’utilisateur Spring Security
//            List<GrantedAuthority> authorities = user.getAuthorities().stream()
//                .map(auth -> new SimpleGrantedAuthority(auth.getName()))
//                .collect(Collectors.toList());
//
//            String userNameAttributeName = userRequest.getClientRegistration()
//                .getProviderDetails()
//                .getUserInfoEndpoint()
//                .getUserNameAttributeName();
//
//            return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
//
////            return new DefaultOAuth2User(
////                authorities,
////                oAuth2User.getAttributes(),
////                "email" // ou "email" ou "sub", selon ce que tu verras dans les logs 🔍
////            );
//
//        } catch (Throwable e) {
//            log.error("❌ Erreur pendant le traitement de OAuth2User : {}", e.getMessage(), e);
//            log.error("⚠️ Classe de l'erreur : {}", e.getClass().getName());
//            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error", e.getMessage(), null), e);
//        }
//
//    }
//}
