package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.RegisterUserRequestDto;
import com.lotto.domain.loginandregister.dto.UserDto;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class LoginAndRegisterFacadeTest {

    private final InMemoryLoginAndRegisterFacadeTestImpl userRepository = new InMemoryLoginAndRegisterFacadeTestImpl();

    UserConformer userConformer = mock(UserConformer.class);

    LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterConfiguration.loginAndRegisterFacade(userRepository);
    UserDetailsService userDetailsService = LoginAndRegisterConfiguration.userDetailsService(userRepository, userConformer);


    User user1 = new User("mail@mail.com", "12345", "token", Set.of("ROLE_USER"));
    User user2 = new User("mail2@mail.com", "12345", "token", Set.of("ROLE_USER"));
    UserDetails userDetails1 = new UserDetailsTestImpl(user1);
    UserDetails userDetails2 = new UserDetailsTestImpl(user2);


    @Test
    public void should_register_two_users_with_different_mail() {
        // given


        // when
        userDetailsService.createUser(userDetails1);
        userDetailsService.createUser(userDetails2);
        // then
        List<UserDto> allUsers = loginAndRegisterFacade.findAllUsers();
        Stream<String> mail = allUsers.stream().map(UserDto::mail);
        assertThat(allUsers).hasSize(2);
        assertThat(mail).containsExactlyInAnyOrder("mail@mail.com", "mail2@mail.com");
    }

    @Test
    public void should_register_two_users_with_identical_mail() {
        // given
        // when
        userDetailsService.createUser(userDetails1);
        // then
        assertThrows(UserAlreadyExistException.class, () -> userDetailsService.createUser(userDetails1));
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
        userDetailsService.createUser(userDetails1);
        userDetailsService.createUser(userDetails2);
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
        userDetailsService.createUser(userDetails1);
        // when
        assertThrows(BadCredentialsException.class, () -> loginAndRegisterFacade.findByEmail("qwe@mail.com"));
        // then
        List<UserDto> allUsers = loginAndRegisterFacade.findAllUsers();
        Stream<String> mail = allUsers.stream().map(UserDto::mail);
        assertThat(mail).containsExactlyInAnyOrder("mail@mail.com");
    }
}