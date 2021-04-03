package ai.aimachineserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AimachineServerApplication

fun main(args: Array<String>) {
    runApplication<AimachineServerApplication>(*args)
}
