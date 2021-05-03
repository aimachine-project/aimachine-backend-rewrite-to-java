package ai.aimachineserver.userlogin

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource

interface UserRepository : CrudRepository<User, Long> {

    @RestResource(exported = false)
    override fun findAll(): MutableIterable<User>

    @RestResource(exported = false)
    fun existsByUsername(username: String): Boolean

    @RestResource(exported = false)
    fun findAllByUsername(username: String): List<User?>
}
