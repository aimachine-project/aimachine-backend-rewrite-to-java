package ai.aimachineserver.users

import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RepositoryRestController
@RequestMapping("/api/users")
class UsersController(
    private val userRepository: UserRepository
) {
    @GetMapping("/self")
    fun getSelf(): ResponseEntity<User> {
        val principal = SecurityContextHolder.getContext().authentication.principal as User
        val userId = principal.id
        return try {
            if (userId != null) {
                val self = userRepository.findById(userId).get()
                ResponseEntity(self, HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: NoSuchElementException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
