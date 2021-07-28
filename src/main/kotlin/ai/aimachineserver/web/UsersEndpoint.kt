package ai.aimachineserver.web

import ai.aimachineserver.application.UserService
import ai.aimachineserver.application.commands.RegisterUserCommand
import ai.aimachineserver.application.dtos.UserDto
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid

@RepositoryRestController
@RequestMapping("/api/users")
class UsersEndpoint(
    private val userService: UserService
) {

    @PostMapping("register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@Valid @RequestBody command: RegisterUserCommand): ResponseEntity<UserDto> {
        val userDto = userService.register(command)
        return if (userDto != null) {
            ResponseEntity(userDto, HttpStatus.CREATED)
        } else {
            ResponseEntity(HttpStatus.CONFLICT)
        }
    }

    @GetMapping("self")
    fun getSelf(): ResponseEntity<UserDto> {
        val userDto = userService.getSelf()
        return if (userDto != null) {
            ResponseEntity(userDto, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
