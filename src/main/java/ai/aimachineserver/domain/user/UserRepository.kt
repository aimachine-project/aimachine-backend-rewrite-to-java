package ai.aimachineserver.domain.user

interface UserRepository {
    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User?
    fun findUserById(id: Long): User?
    fun save(user: User): User
}
