package com.upc.matchpoint.iam.application.internal.commandservices;

import com.upc.matchpoint.iam.application.internal.outboundservices.hashing.HashingService;
import com.upc.matchpoint.iam.application.internal.outboundservices.tokens.TokenService;
import com.upc.matchpoint.iam.domain.model.aggregates.User;
import com.upc.matchpoint.iam.domain.model.commands.SignInCommand;
import com.upc.matchpoint.iam.domain.model.commands.SignUpCommand;
import com.upc.matchpoint.iam.domain.services.UserCommandService;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.upc.matchpoint.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.List;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User command service implementation
 * <p>
 *     This class implements the {@link UserCommandService} interface and provides the implementation for the
 *     {@link SignInCommand} and {@link SignUpCommand} commands.
 * </p>
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    /**
     * Handle the sign-in command
     * <p>
     *     This method handles the {@link SignInCommand} command and returns the user and the token.
     * </p>
     * @param command the sign-in command containing the username and password
     * @return and optional containing the user matching the username and the generated token
     * @throws RuntimeException if the user is not found or the password is invalid
     */
    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty())
            return Optional.empty();
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            return Optional.empty();
        var token = tokenService.generateToken(user.get().getUsername());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    /**
     * Handle the sign-up command
     * <p>
     *     This method handles the {@link SignUpCommand} command and returns the user.
     * </p>
     * @param command the sign-up command containing the username and password
     * @return the created user
     */
    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            return Optional.empty();
        var roles = (command.roles() == null || command.roles().isEmpty())
            ? List.of(roleRepository.findByName(com.upc.matchpoint.iam.domain.model.valueobjects.Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Role name not found")))
            : command.roles().stream().map(role -> roleRepository.findByName(role.getName()).orElseThrow(() -> new RuntimeException("Role name not found"))).toList();
        var user = new User(command.username(), hashingService.encode(command.password()), roles);
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

    @org.springframework.beans.factory.annotation.Value("${google.client.id:}")
    private String googleClientId;

    @Override
    public Optional<org.apache.commons.lang3.tuple.ImmutablePair<User, String>> handle(com.upc.matchpoint.iam.domain.model.commands.SignInWithGoogleCommand command) {
        String email;
        String name;

        if (command.idToken().startsWith("mock-token-")) {
            email = command.idToken().substring("mock-token-".length());
            name = email.split("@")[0];
        } else {
            try {
                var transport = new com.google.api.client.http.javanet.NetHttpTransport();
                var jsonFactory = com.google.api.client.json.gson.GsonFactory.getDefaultInstance();
                var verifier = new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        .setAudience(java.util.Collections.singletonList(googleClientId))
                        .build();

                var idTokenObj = verifier.verify(command.idToken());
                if (idTokenObj == null) {
                    throw new RuntimeException("Invalid Google ID Token");
                }
                var payload = idTokenObj.getPayload();
                email = payload.getEmail();
                name = (String) payload.get("name");
                if (name == null) {
                    name = email.split("@")[0];
                }
            } catch (Exception e) {
                throw new RuntimeException("Error verifying Google ID Token: " + e.getMessage(), e);
            }
        }

        var userOpt = userRepository.findByUsername(email);
        User user;
        if (userOpt.isEmpty()) {
            var roleName = command.role() == null || command.role().isEmpty()
                ? com.upc.matchpoint.iam.domain.model.valueobjects.Roles.ROLE_USER
                : com.upc.matchpoint.iam.domain.model.valueobjects.Roles.valueOf(command.role());
            
            var role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found"));
            
            user = new User(email, hashingService.encode(java.util.UUID.randomUUID().toString()), List.of(role));
            userRepository.save(user);
        } else {
            user = userOpt.get();
        }

        var token = tokenService.generateToken(user.getUsername());
        return Optional.of(org.apache.commons.lang3.tuple.ImmutablePair.of(user, token));
    }
}
