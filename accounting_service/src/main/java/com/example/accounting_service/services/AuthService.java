package com.example.accounting_service.services;

import com.example.accounting_service.dto.JwtAuthenticationResponse;
import com.example.accounting_service.dto.UserDto;
import com.example.accounting_service.entities.AuthProvider;
import com.example.accounting_service.entities.User;
import com.example.accounting_service.entities.UserRole;
import com.example.accounting_service.exceptions.AccountLockedException;
import com.example.accounting_service.exceptions.EmailAlreadyExistsException;
import com.example.accounting_service.repositories.UserRepository;
import com.example.accounting_service.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuthService {
    private static final int MAX_ATTEMPTS = 2;
    private static final long LOCK_TIME_MINUTES = 1;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockedAccounts = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public JwtAuthenticationResponse authenticateUser(UserDto userDto) {
        if (this.isAccountLocked(userDto.getEmail())) {
            long remainingSeconds = this.getRemainingLockTimeSeconds(userDto.getEmail());
            throw new AccountLockedException("Account is locked due to too many failed attempts. Try again later.",
                    remainingSeconds);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getEmail(),
                            userDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            this.loginSucceeded(userDto.getEmail());

            return new JwtAuthenticationResponse(jwt, userDto.getEmail());

        } catch (BadCredentialsException e) {
            this.loginFailed(userDto.getEmail());
            throw e;
        }
    }

    public void registerUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        userRepository.save(this.createUser(userDto));
    }

    private User createUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setAuthProvider(userDto.getAuthProvider() != null
                ? userDto.getAuthProvider()
                : AuthProvider.DEFAULT.getValue());

        if (Objects.equals(user.getAuthProvider(), AuthProvider.DEFAULT.getValue())) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        BigDecimal balance = userDto.getAccount() == null
                ? BigDecimal.ZERO
                : userDto.getAccount();
        user.setAccount(balance);

        user.setRole(
                user.getRole() != null &&
                        (user.getRole().equals(UserRole.USER.getValue()) ||
                                user.getRole().equals(UserRole.ADMIN.getValue()))
                        ? user.getRole()
                        : UserRole.USER.getValue()
        );
        return user;
    }

    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
        lockedAccounts.remove(email);
    }

    public void loginFailed(String email) {
        int attempts = attemptsCache.getOrDefault(email, 0);
        attempts++;
        attemptsCache.put(email, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockAccount(email);
        }
    }

    private void lockAccount(String email) {
        long lockUntilTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(LOCK_TIME_MINUTES);
        lockedAccounts.put(email, lockUntilTime);
    }

    public boolean isAccountLocked(String email) {
        Long lockTime = lockedAccounts.get(email);
        if (lockTime == null) {
            return false;
        }

        if (System.currentTimeMillis() > lockTime) {
            lockedAccounts.remove(email);
            attemptsCache.remove(email);
            log.info("Account is unlocked. Email: {}", email);
            return false;
        }

        return true;
    }

    public long getRemainingLockTimeSeconds(String email) {
        Long lockTime = lockedAccounts.get(email);
        if (lockTime == null) {
            return 0;
        }

        long remainingMillis = lockTime - System.currentTimeMillis();
        return remainingMillis > 0 ? TimeUnit.MILLISECONDS.toSeconds(remainingMillis) : 0;
    }
}
