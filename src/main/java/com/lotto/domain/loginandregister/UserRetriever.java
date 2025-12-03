package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;

@AllArgsConstructor
@Log4j2
class UserRetriever {

    private final UserRepository userRepository;

    UserDto findByEmail(final String email) {
        User userByEmail = userRepository.findFirstByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User with email: {} not found", email);
                    return new BadCredentialsException(email);
                });
        return UserDto.builder()
                .userId(userByEmail.getUserId())
                .mail(userByEmail.getEmail())
                .password(userByEmail.getPassword())
                .build();
    }

    List<UserDto> findAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(user -> UserDto.builder()
                        .userId(user.getUserId())
                        .mail(user.getEmail())
                        .password(user.getPassword())
                        .build())
                .toList();
    }
}
