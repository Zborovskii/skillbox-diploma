package ru.skillbox.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.dto.AuthResponse;
import ru.skillbox.dto.AuthorizeUserRequest;
import ru.skillbox.dto.CaptchaResponse;
import ru.skillbox.dto.PasswordResetRequest;
import ru.skillbox.dto.RegisterUserRequest;
import ru.skillbox.dto.RestorePasswordRequest;
import ru.skillbox.dto.ResultResponse;
import ru.skillbox.services.ResponseService;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private ResponseService responseService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthorizeUserRequest authorizeUserRequest) {
        return responseService.login(authorizeUserRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterUserRequest registerUserRequest) {
        return responseService.registerUser(registerUserRequest);
    }

    @PostMapping("/password")
    public ResponseEntity<ResultResponse> resetPassword(@RequestBody PasswordResetRequest request) {
        return responseService.resetUserPassword(request);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResultResponse> logout() {
        return responseService.logout();
    }

    @GetMapping("/check")
    public ResponseEntity<AuthResponse> authCheck() {
        return responseService.getAuthorizedUserResponse();
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultResponse> restore(@RequestBody @Valid RestorePasswordRequest request) {
        return responseService.restorePassword(request.getEmail());
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        return responseService.getCaptchaResponse();
    }

    /**общие замечания по авторизации:
     * нужно использовать spring security
     * сейчас часть логики по формированию ответа вынесена в сервис ResponseService, что не правильно этот
     * сервис разросся в помойку и всю логику касаемую авторизации нужно вынести в AuthService
     */

}
