package ai.aimachineserver.config.websocket

import ai.aimachineserver.application.GameFactoryClassicTicTacToe
import ai.aimachineserver.application.GameFactorySoccer
import ai.aimachineserver.application.GameFactoryTicTacToeNFields
import ai.aimachineserver.application.WebSocketServerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketServerHandlerClassicTicTacToe(), "/games/tictactoe").setAllowedOrigins("*")
        registry.addHandler(webSocketServerHandlerTicTacToeNFields(), "/games/tictactoenfields").setAllowedOrigins("*")
        registry.addHandler(webSocketServerHandlerSoccer(), "/games/soccer").setAllowedOrigins("*")

        // add another url for game with ai 
        // add boolean argument to WebSocketHandler representing isGameWithAi
        // registry.addHandler(webSocketServerHandlerSoccer(true), "/games/soccer/ai").setAllowedOrigins("*")
    }

    @Bean
    fun webSocketServerHandlerClassicTicTacToe(): WebSocketHandler {
        return WebSocketServerHandler(GameFactoryClassicTicTacToe())
    }

    @Bean
    fun webSocketServerHandlerTicTacToeNFields(): WebSocketHandler {
        return WebSocketServerHandler(GameFactoryTicTacToeNFields())
    }

    @Bean
    fun webSocketServerHandlerSoccer(): WebSocketHandler {
        return WebSocketServerHandler(GameFactorySoccer())
    }
}
