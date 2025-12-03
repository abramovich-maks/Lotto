package com.lotto.domain.loginandregister;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Getter
@Setter
@Document
public class User {

    @Id
    private String userId;

    @Indexed(unique = true)
    private String email;

    private String password;

    private Collection<String> authorities;

    private String confirmationToken;

    private boolean enabled = false;

    public boolean confirm() {
        this.setEnabled(true);
        this.setConfirmationToken(null);
        return true;
    }

    public User(final String email, final String password, String confirmationToken, final Collection<String> authorities) {
        this.email = email;
        this.password = password;
        this.confirmationToken = confirmationToken;
        this.authorities = authorities;
    }
}
