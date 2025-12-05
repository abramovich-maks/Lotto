package com.lotto.domain.loginandregister;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoginAndRegisterConfiguration {

    @Bean
    public static LoginAndRegisterFacade loginAndRegisterFacade(UserRepository userRepository) {
        UserRetriever userRetriever = new UserRetriever(userRepository);
        return new LoginAndRegisterFacade(userRetriever);
    }

    @Bean
    public static UserDetailsService userDetailsService(UserRepository userRepository, UserConformer userConformer) {
        return new UserDetailsService(userRepository, userConformer);
    }
}
