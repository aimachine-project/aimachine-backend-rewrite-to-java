package ai.aimachineserver.infrastructure;

import ai.aimachineserver.domain.user.User;
import ai.aimachineserver.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

interface UserCrudRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findUserById(Long id);
}

@Repository
class UserRepositoryImpl implements UserRepository {

    private final UserCrudRepository crudRepo;

    @Autowired
    UserRepositoryImpl(UserCrudRepository crudRepo) {
        this.crudRepo = crudRepo;
    }

    @Override
    public boolean existsByUsername(String username) {
        return crudRepo.existsByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        return crudRepo.findByUsername(username);
    }

    @Override
    public User findUserById(Long id) {
        return crudRepo.findUserById(id);
    }

    @Override
    public User save(User user) {
        return crudRepo.save(user);
    }
}
