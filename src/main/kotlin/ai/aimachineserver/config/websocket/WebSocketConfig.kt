package ai.aimachineserver.config.websocket

import ai.aimachineserver.application.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(private val aiCallService: AiCallService) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketServerHandlerClassicTicTacToe(), "/games/tictactoe").setAllowedOrigins("*")
        registry.addHandler(webSocketServerHandlerTicTacToeNFields(), "/games/tictactoenfields").setAllowedOrigins("*")
        registry.addHandler(webSocketServerHandlerSoccer(), "/games/soccer").setAllowedOrigins("*")
    }

    @Bean
    fun webSocketServerHandlerClassicTicTacToe(): WebSocketHandler {
        return WebSocketServerHandler(GameFactoryClassicTicTacToe(), aiCallService)
    }

    @Bean
    fun webSocketServerHandlerTicTacToeNFields(): WebSocketHandler {
        return WebSocketServerHandler(GameFactoryTicTacToeNFields(), aiCallService)
    }

    @Bean
    fun webSocketServerHandlerSoccer(): WebSocketHandler {
        return WebSocketServerHandler(GameFactorySoccer(), aiCallService)
    }
}
