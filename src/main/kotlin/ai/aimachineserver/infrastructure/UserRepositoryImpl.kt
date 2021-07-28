package ai.aimachineserver.infrastructure

import ai.aimachineserver.domain.user.User
import ai.aimachineserver.domain.user.UserRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface UserCrudRepository : CrudRepository<User, Long> {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User?
    fun findUserById(id: Long): User?
}

@Repository
class UserRepositoryImpl(
    private val crudRepo: UserCrudRepository
) : UserRepository {
    override fun existsByUsername(username: String): Boolean = crudRepo.existsByUsername(username)
    override fun findByUsername(username: String): User? = crudRepo.findByUsername(username)
    override fun findUserById(id: Long): User? = crudRepo.findUserById(id)
    override fun save(user: User): User = crudRepo.save(user)
}