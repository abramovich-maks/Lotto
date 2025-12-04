package com.lotto.infrastructure.usercrud;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping()
@Log4j2
class RegisterController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder bCryptpasswordEncoder;


    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(@RequestBody RegisterUserRequestDto dto) {
    String encodedPassword = bCryptpasswordEncoder.encode(dto.password());
        String username = dto.email();
        UserDetails user = User.builder()
                .username(username)
                .password(encodedPassword)
                .authorities("ROLE_USER")
                .build();
        userDetailsManager.createUser(user);
        return ResponseEntity.ok(new RegisterUserResponseDto("Success. User created."));
    }
}
