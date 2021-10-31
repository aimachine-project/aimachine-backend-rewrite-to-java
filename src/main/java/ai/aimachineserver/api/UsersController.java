package ai.aimachineserver.api;

import ai.aimachineserver.application.UserService;
import ai.aimachineserver.application.commands.CreateUserCommand;
import ai.aimachineserver.application.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RepositoryRestController
@RequestMapping("/api/users")
class UsersController {

    private final UserService userService;

    @Autowired
    UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> create(
            @Valid
            @RequestBody
                    CreateUserCommand command
    ) {
        UserDto userDto = userService.createUser(command);
        if (userDto != null) {
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("self")
    ResponseEntity<UserDto> getSelf() {
        UserDto userDto = userService.getSelf();
        if (userDto != null) {
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
