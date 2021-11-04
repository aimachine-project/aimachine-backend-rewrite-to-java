package ai.aimachineserver.domain.user;

public interface UserRepository {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findUserById(Long id);

    User save(User user);
}
