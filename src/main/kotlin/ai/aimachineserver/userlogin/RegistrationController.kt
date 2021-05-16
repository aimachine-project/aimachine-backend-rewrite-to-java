package ai.aimachineserver.userlogin

import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RepositoryRestController
@RequestMapping("/users")
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
            val user = userRepository.findAllByUsername(username).single()
            if (user != null && passwordEncoder.matches(password, user.password)) {
                ResponseEntity(user, HttpStatus.OK)
            } else {
                ResponseEntity(HttpStatus.NOT_FOUND)
            }
        } catch (e: NoSuchElementException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }

    @PostMapping(consumes = ["application/json"])
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
