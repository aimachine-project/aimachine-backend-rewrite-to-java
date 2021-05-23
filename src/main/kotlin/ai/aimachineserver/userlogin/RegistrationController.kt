package ai.aimachineserver.userlogin

import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RepositoryRestController
@RequestMapping("/api/register")
class RegistrationController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @GetMapping
    fun getUsers(
        @RequestParam("username") username: String,
        @RequestParam("password") password: String
    ): ResponseEntity<User> {
        return try {
            val user = userRepository.findByUsername(username)
            if (user != null && passwordEncoder.matches(password, user.password)) {
                ResponseEntity(user, HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: UsernameNotFoundException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postUser(@RequestBody user: User): ResponseEntity<User> {
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
