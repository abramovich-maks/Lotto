package com.lotto.infrastructure.security.jwt.lotto;

import com.lotto.domain.loginandregister.LoginAndRegisterFacade;
import com.lotto.domain.loginandregister.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
class JwtTokenController {

    private final JwtTokenGenerator tokenGenerator;
    private final UserDetailsService userDetailsService;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> authenticateAndGenerateToken(@RequestBody TokenRequestDto dto, HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        String token = tokenGenerator.authenticateAndGenerateToken(dto.login(), dto.password());
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return ResponseEntity.ok().body(
                JwtResponseDto.builder()
                        .token(token)
                        .build()
        );
    }
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> authStatus(HttpServletRequest request) {
        boolean isLoggedIn = userDetailsService.isIsLoggedIn(request);
        return ResponseEntity.ok(Map.of("loggedIn", isLoggedIn));
    }
}
