package ai.aimachineserver.api;

import ai.aimachineserver.application.UserService;
import ai.aimachineserver.application.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/login")
@RestController
class LoginController {
    private final UserService userService;

    @Autowired
    LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<UsernameWrapper> login() {
        UserDto user = userService.getSelf();
        if (user != null) {
            return new ResponseEntity<>(new UsernameWrapper(user.getUsername()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private static class UsernameWrapper {
        String username;

        UsernameWrapper(String username) {
            this.username = username;
        }
    }
}
