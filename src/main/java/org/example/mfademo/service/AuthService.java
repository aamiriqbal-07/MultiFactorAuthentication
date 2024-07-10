package org.example.mfademo.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.example.mfademo.dto.LoginRequest;
import org.example.mfademo.dto.SignUpRequest;
import org.example.mfademo.model.User;
import org.example.mfademo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        user.setMfaSecret(key.getKey());

        userRepository.save(user);
    }

    public User signIn(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return user;
        }
        return null;
    }

    public String generateQrCodeUrl(User user) {
        String format = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
        return String.format(format, "MfaDemo", user.getUsername(), user.getMfaSecret(), "MfaDemo");
    }

    public String getUserMfaSecret(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getMfaSecret();
        }
        throw new UsernameNotFoundException("User not found");
    }

    public boolean verifyMfaToken(String username, String token) {
        String secret = getUserMfaSecret(username);

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, token);
    }
}
