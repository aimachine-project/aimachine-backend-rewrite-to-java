package ai.aimachineserver.userlogin

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?
}
