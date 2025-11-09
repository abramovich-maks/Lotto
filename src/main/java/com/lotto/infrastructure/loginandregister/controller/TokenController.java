package com.lotto.infrastructure.loginandregister.controller;

import com.lotto.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import com.lotto.infrastructure.loginandregister.controller.dto.TokenRequestDto;
import com.lotto.infrastructure.security.jwt.JwtAuthenticator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
class TokenController {

    private final JwtAuthenticator jwtAuthenticator;

    @PostMapping("/token")
    public ResponseEntity<JwtResponseDto> getToken(@Valid @RequestBody TokenRequestDto requestDto) {
        final JwtResponseDto jwtResponseDto = jwtAuthenticator.authenticateAndGenerateToken(requestDto);
        return ResponseEntity.ok(jwtResponseDto);
    }
}
