package com.lotto.infrastructure.loginandregister.controller;

import com.lotto.domain.loginandregister.LoginAndRegisterFacade;
import com.lotto.domain.loginandregister.dto.RegisterUserRequestDto;
import com.lotto.domain.loginandregister.dto.RegisterUserResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
class RegisterController {

    LoginAndRegisterFacade loginAndRegisterFacade;
    private final PasswordEncoder bCryptpasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerNewUser(@Valid @RequestBody RegisterUserRequestDto requestDto) {
        String encodedPassword = bCryptpasswordEncoder.encode(requestDto.password());
        RegisterUserResponseDto register = loginAndRegisterFacade.register(new RegisterUserRequestDto(requestDto.email(), encodedPassword));
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
    }
}
