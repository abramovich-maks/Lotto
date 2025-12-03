package com.lotto.domain.loginandregister;

import com.lotto.domain.loginandregister.dto.UserDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class LoginAndRegisterFacade {

    private final UserRetriever userRetriever;

    public UserDto findByEmail(String email) {
        return userRetriever.findByEmail(email);
    }

    public List<UserDto> findAllUsers() {
        return userRetriever.findAllUsers();
    }

}
