package ai.aimachineserver.userlogin

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, UUID> {

    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): User?
}
