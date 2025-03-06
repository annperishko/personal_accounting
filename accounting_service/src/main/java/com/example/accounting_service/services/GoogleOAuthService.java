package com.example.accounting_service.services;

import com.example.accounting_service.dto.JwtAuthenticationResponse;
import com.example.accounting_service.entities.AuthProvider;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.entities.UserRole;
import com.example.accounting_service.exceptions.OAuthAuthenticationException;
import com.example.accounting_service.repositories.UserRepository;
import com.example.accounting_service.security.JwtTokenProvider;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
public class GoogleOAuthService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList("email profile");

    @Value("${google.oauth.client.id}")
    private String clientId;

    @Value("${google.oauth.client.secret}")
    private String clientSecret;

    @Value("${google.oauth.redirect.uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public GoogleOAuthService(UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public String generateAuthorizationUrl() throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
                .setWeb(new GoogleClientSecrets.Details()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRedirectUris(Collections.singletonList(redirectUri)));

        AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri);

        return authorizationUrl.build();
    }

    public JwtAuthenticationResponse processCode(String code) throws IOException {
        try {
            GoogleIdToken idToken = this.getGoogleIdToken(code);

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            if (email == null || email.isEmpty()) {
                throw new OAuthAuthenticationException("Email not provided by Google OAuth");
            }

            String name = (String) payload.get("name");
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isEmpty()) {
                User user = new User();
                user.setEmail(email);
                user.setName(firstName != null ? firstName : name);
                user.setSurname(lastName != null ? lastName : "");
                user.setAuthProvider(AuthProvider.GOOGLE.getValue());
                user.setRole(UserRole.USER.getValue());
                user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                user.setAccount(ZERO);
                userRepository.save(user);
            }

            GrantedAuthority authority = new SimpleGrantedAuthority(UserRole.USER.name());
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(email)
                    .password("")
                    .authorities(authority)
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    Collections.singletonList(authority));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            return new JwtAuthenticationResponse(jwt, email);

        } catch (GeneralSecurityException e) {
            throw new OAuthAuthenticationException("Security error during OAuth processing: " + e.getMessage(), e);
        } catch (Exception e) {
            if (e instanceof OAuthAuthenticationException) {
                throw e;
            }
            throw new OAuthAuthenticationException("Unexpected error during OAuth processing: " + e.getMessage(), e);
        }
    }

    private GoogleIdToken getGoogleIdToken(String code) throws IOException, GeneralSecurityException {
        TokenResponse tokenResponse;

        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
                .setWeb(new GoogleClientSecrets.Details()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRedirectUris(Collections.singletonList(redirectUri)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();

        tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();


        if (tokenResponse.get("id_token") == null) {
            throw new OAuthAuthenticationException("No ID token returned from Google");
        }

        String idTokenString = tokenResponse.get("id_token").toString();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, JSON_FACTORY)
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new OAuthAuthenticationException("Invalid ID token");
            }
        } catch (Exception e) {
            throw new OAuthAuthenticationException("Token verification failed: " + e.getMessage());
        }

        return idToken;
    }
}