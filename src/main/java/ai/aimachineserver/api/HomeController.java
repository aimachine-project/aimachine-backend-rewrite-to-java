package ai.aimachineserver.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HomeController {
    @GetMapping
    String homeMessage() {
        return "Greetings from AIMachine Backend service";
    }
}
