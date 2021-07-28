package ai.aimachineserver.web

import ai.aimachineserver.domain.user.User
import ai.aimachineserver.domain.user.UserRepository
import ai.aimachineserver.utils.SecurityUtils
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RepositoryRestController
@RequestMapping("/api/users")
class UsersEndpoint(
    private val userRepository: UserRepository,
    private val securityUtils: SecurityUtils
) {
    @GetMapping("/self")
    fun getSelf(): ResponseEntity<User> {
        val userId = securityUtils.getLoggedUserId()
        return try {
            if (userId != null) {
                val self = userRepository.findUserById(userId)
                ResponseEntity(self, HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: NoSuchElementException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
