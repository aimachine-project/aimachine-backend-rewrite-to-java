package ai.aimachineserver.web

import ai.aimachineserver.application.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/login")
@RestController
class LoginEndpoint(private val userService: UserService) {
    @GetMapping
    private fun login(): ResponseEntity<UsernameWrapper> {
        val username = userService.getSelf()?.username
        return if (username != null) {
            ResponseEntity(UsernameWrapper(username), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    class UsernameWrapper(val username: String)
}
