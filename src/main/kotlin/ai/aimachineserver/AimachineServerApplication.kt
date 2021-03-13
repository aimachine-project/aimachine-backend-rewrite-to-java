package ai.aimachineserver

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AimachineServerApplication {

    @Bean
    fun socketIOServer(): SocketIOServer? {
        val config = Configuration()
        config.hostname = "localhost"
        config.port = 9090
        val server = SocketIOServer(config)

        server.addConnectListener {
            println("Client connected")
        }

        server.addDisconnectListener {
            println("client disconnected")
        }

        return server
    }
}

fun main(args: Array<String>) {
    runApplication<AimachineServerApplication>(*args)
}
