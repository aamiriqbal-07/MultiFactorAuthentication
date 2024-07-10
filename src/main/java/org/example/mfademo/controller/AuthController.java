package org.example.mfademo.controller;

import com.google.zxing.WriterException;
import org.example.mfademo.dto.MfaTokenVerificationRequest;
import org.example.mfademo.service.AuthService;
import org.example.mfademo.service.QrCodeService;
import org.example.mfademo.dto.LoginRequest;
import org.example.mfademo.dto.SignUpRequest;
import org.example.mfademo.model.User;
import org.example.mfademo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody LoginRequest loginRequest) {
        User user = authService.signIn(loginRequest);
        if (user != null) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/generate-qr-code")
    public ResponseEntity<byte[]> generateQrCode(@RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            String qrCodeUrl = authService.generateQrCodeUrl(user);
            byte[] qrCodeImage = qrCodeService.generateQrCode(qrCodeUrl, 200, 200);
            return ResponseEntity.ok().header("Content-Type", "image/png").body(qrCodeImage);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyMfaToken(@RequestBody MfaTokenVerificationRequest tokenVerificationRequest) {
        boolean isTokenValid = authService.verifyMfaToken(tokenVerificationRequest.getUsername(), tokenVerificationRequest.getMfaToken());
        if (isTokenValid) {
            return ResponseEntity.status(HttpStatus.OK).body("MFA token verified. Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid MFA token. Login failed.");
        }
    }
}
