package ai.aimachineserver.application;

import ai.aimachineserver.application.commands.CreateUserCommand;
import ai.aimachineserver.application.dtos.UserDto;
import ai.aimachineserver.config.AppConfig;
import ai.aimachineserver.domain.user.User;
import ai.aimachineserver.domain.user.UserRepository;
import ai.aimachineserver.domain.user.UserRole;
import ai.aimachineserver.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SecurityUtils securityUtils,
            AppConfig appConfig
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;

        String adminUsername = appConfig.dbUserAdminUsername;
        if (!userRepository.existsByUsername(adminUsername)) {
            String adminPassword = appConfig.dbUserAdminPassword;
            User adminUser = new User(adminUsername, passwordEncoder.encode(adminPassword), UserRole.ADMIN.roleName);
            userRepository.save(adminUser);
        }
    }

    public UserDto createUser(CreateUserCommand command) {
        boolean userAlreadyExists = userRepository.existsByUsername(command.username);
        if (userAlreadyExists) {
            return null;
        }
        String encodedPassword = passwordEncoder.encode(command.password);
        User newUser = new User(command.username, encodedPassword);
        return dto(userRepository.save(newUser));
    }

    public UserDto getSelf() {
        Long userId = securityUtils.getLoggedUserId();
        User user;
        if (userId != null) {
            try {
                user = userRepository.findUserById(userId);
            } catch (NoSuchElementException e) {
                return null;
            }
            if (user == null) return null;
            return dto(user);
        } else {
            return null;
        }
    }

    private UserDto dto(User user) {
        List<String> userRoles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new UserDto(
                user.id,
                user.getUsername(),
                userRoles
        );
    }
}
