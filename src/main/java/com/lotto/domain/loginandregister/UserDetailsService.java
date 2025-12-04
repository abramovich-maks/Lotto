package com.lotto.domain.loginandregister;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
public class UserDetailsService implements UserDetailsManager {

    private final UserRepository userRepository;
    private final UserConformer userConformer;


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void createUser(final UserDetails user) {
        if (userExists(user.getUsername())) {
            log.warn("User with username: {} already exists", user.getUsername());
            throw new UserAlreadyExistException(user.getUsername());
        }
        String confirmationToken = UUID.randomUUID().toString();
        List<String> authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        User createdUser = new User(
                user.getUsername(),
                user.getPassword(),
                confirmationToken,
                authorities
        );
        User savedUser = userRepository.save(createdUser);
        log.warn("Saved user with id: {}", savedUser.getUserId());
        userConformer.sendConfirmationEmail(createdUser);
    }

    @Override
    public void updateUser(final UserDetails user) {

    }

    @Override
    public void deleteUser(final String username) {

    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

    }

    @Override
    public boolean userExists(final String username) {
        return userRepository.existsByEmail(username);
    }
}
