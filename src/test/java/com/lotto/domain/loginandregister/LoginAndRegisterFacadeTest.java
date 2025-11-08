package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.RegisterUserRequestDto;
import com.lotto.domain.loginandregister.dto.UserDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginAndRegisterFacadeTest {

    private final InMemoryLoginAndRegisterFacadeTestImpl userRepository = new InMemoryLoginAndRegisterFacadeTestImpl();
    LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterConfiguration.loginAndRegisterFacade(userRepository);

    @Test
    public void should_register_two_users_with_different_mail() {
        // given
        RegisterUserRequestDto userRequestDto = RegisterUserRequestDto.builder()
                .email("mail@mail.com")
                .password("12345")
                .build();
        RegisterUserRequestDto userRequestDto2 = RegisterUserRequestDto.builder()
                .email("mail2@mail.com")
                .password("12345")
                .build();
        // when
        loginAndRegisterFacade.register(userRequestDto);
        loginAndRegisterFacade.register(userRequestDto2);
        // then
        List<UserDto> allUsers = loginAndRegisterFacade.findAllUsers();
        Stream<String> mail = allUsers.stream().map(UserDto::mail);
        assertThat(allUsers).hasSize(2);
        assertThat(mail).containsExactlyInAnyOrder("mail@mail.com", "mail2@mail.com");
    }

    @Test
    public void should_register_two_users_with_identical_mail() {
        // given
        RegisterUserRequestDto userRequestDto = RegisterUserRequestDto.builder()
                .email("mail@mail.com")
                .password("12345")
                .build();
        RegisterUserRequestDto userRequestDto2 = RegisterUserRequestDto.builder()
                .email("mail@mail.com")
                .password("12345")
                .build();
        // when
        loginAndRegisterFacade.register(userRequestDto);
        // then
        assertThrows(UserAlreadyExistException.class, () -> loginAndRegisterFacade.register(userRequestDto2));
        List<UserDto> allUsers = loginAndRegisterFacade.findAllUsers();
        Stream<String> mail = allUsers.stream().map(UserDto::mail);
        assertThat(allUsers).hasSize(1);
        assertThat(mail).containsExactlyInAnyOrder("mail@mail.com");
    }

    @Test
    public void should_user_by_email() {
        // given
        RegisterUserRequestDto userRequestDto = RegisterUserRequestDto.builder()
                .email("mail@mail.com")
                .password("12345")
                .build();
        RegisterUserRequestDto userRequestDto2 = RegisterUserRequestDto.builder()
                .email("mail2@mail.com")
                .password("12345")
                .build();
        loginAndRegisterFacade.register(userRequestDto);
        loginAndRegisterFacade.register(userRequestDto2);
        // when
        UserDto userByEmail = loginAndRegisterFacade.findByEmail(userRequestDto.email());
        // then
        List<UserDto> allUsers = loginAndRegisterFacade.findAllUsers();
        Stream<String> mail = allUsers.stream().map(UserDto::mail);
        assertThat(mail).containsExactlyInAnyOrder("mail@mail.com", "mail2@mail.com");
        AssertionsForClassTypes.assertThat(userByEmail.mail()).isEqualTo("mail@mail.com");
    }

    @Test
    public void should_user_by_email_is_not_exist() {
        // given
        RegisterUserRequestDto userRequestDto = RegisterUserRequestDto.builder()
                .email("mail@mail.com")
                .password("12345")
                .build();
        loginAndRegisterFacade.register(userRequestDto);
        // when
        assertThrows(BadCredentialsException.class, () -> loginAndRegisterFacade.findByEmail("qwe@mail.com"));
        // then
        List<UserDto> allUsers = loginAndRegisterFacade.findAllUsers();
        Stream<String> mail = allUsers.stream().map(UserDto::mail);
        assertThat(mail).containsExactlyInAnyOrder("mail@mail.com");}
}