package ai.aimachineserver.users

import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RepositoryRestController
@RequestMapping("/api/register")
class RegistrationController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun registerUser(@RequestBody user: User): ResponseEntity<User> {
        val userAlreadyExists = userRepository.existsByUsername(user.username)
        return if (userAlreadyExists) {
            return ResponseEntity<User>(HttpStatus.CONFLICT)
        } else {
            val encodedPassword = passwordEncoder.encode(user.password)
            val newUser = User(user.username, encodedPassword)
            ResponseEntity(userRepository.save(newUser), HttpStatus.CREATED)
        }
    }
}
