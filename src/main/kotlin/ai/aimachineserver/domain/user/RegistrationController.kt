package ai.aimachineserver.domain.user

import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RepositoryRestController
@RequestMapping("/api/register")
class RegistrationController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    init {
        val adminUsername = System.getenv("DB_AIM_USER_ROLE_ADMIN_USERNAME")
        if (adminUsername != null && !userRepository.existsByUsername(adminUsername)) {
            val adminPassword = System.getenv("DB_AIM_USER_ROLE_ADMIN_PASSWORD")
            val adminUser = User(adminUsername, passwordEncoder.encode(adminPassword), UserRole.ADMIN.roleName)
            userRepository.save(adminUser)
        }
    }

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
