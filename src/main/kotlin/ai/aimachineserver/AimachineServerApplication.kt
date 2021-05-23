package ai.aimachineserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class AimachineServerApplication{
    @Bean
    fun encoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
    runApplication<AimachineServerApplication>(*args)
}
