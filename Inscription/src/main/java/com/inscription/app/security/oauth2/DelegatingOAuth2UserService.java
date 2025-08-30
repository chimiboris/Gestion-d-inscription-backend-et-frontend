package com.inscription.app.security.oauth2;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import static com.inscription.app.InscriptionApp.log;


@Service
public class DelegatingOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AzureOidcUserService azureOidcUserService;

    public DelegatingOAuth2UserService(CustomOAuth2UserService customOAuth2UserService,
                                       AzureOidcUserService azureOidcUserService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.azureOidcUserService = azureOidcUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId().toLowerCase();

        log.info("🧭 DelegatingOAuth2UserService: registrationId = {}", registrationId); // <--- ICI

        switch (registrationId) {
            case "google":
                return customOAuth2UserService.loadUser(userRequest);

            case "azure":
                if (userRequest instanceof OidcUserRequest oidcUserRequest) {
                    return azureOidcUserService.loadUser(oidcUserRequest);
                } else {
                    throw new OAuth2AuthenticationException("Azure requires OIDC but request is not an OidcUserRequest.");
                }

            default:
                throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + registrationId);
        }
    }
}




//@Service
//public class DelegatingOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final CustomOAuth2UserService customOAuth2UserService;
//    private final AzureOidcUserService azureService;
//
//    public DelegatingOAuth2UserService(CustomOAuth2UserService customOAuth2UserService, AzureOidcUserService azureService) {
//        this.customOAuth2UserService = customOAuth2UserService;
//        this.azureService = azureService;
//    }
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        switch (registrationId.toLowerCase()) {
//            case "google":
//                return customOAuth2UserService.loadUser(userRequest);
//            case "azure":
//                return azureService.loadUser((OidcUserRequest) userRequest);
//            default:
//                throw new OAuth2AuthenticationException("Provider not supported: " + registrationId);
//        }
//    }
//}

