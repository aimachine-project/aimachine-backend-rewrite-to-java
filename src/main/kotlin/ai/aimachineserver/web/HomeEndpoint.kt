package ai.aimachineserver.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeEndpoint {
    @GetMapping
    fun homeMessage(): String {
        return "Greetings from AIMachine Backend service"
    }
}
