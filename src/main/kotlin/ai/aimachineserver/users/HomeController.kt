package ai.aimachineserver.users

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class HomeController {
    @GetMapping
    fun homeMessage(): String {
        return "Greetings from AIMachine Backend service"
    }
}
