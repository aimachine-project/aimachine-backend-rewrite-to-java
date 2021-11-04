package ai.aimachineserver.config.websocket;

import ai.aimachineserver.application.AiCallService;
import ai.aimachineserver.application.WebSocketServerHandler;
import ai.aimachineserver.domain.games.GameFactoryClassicTicTacToe;
import ai.aimachineserver.domain.games.GameFactorySoccer;
import ai.aimachineserver.domain.games.GameFactoryTicTacToeNFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
class WebSocketConfig implements WebSocketConfigurer {

    private final AiCallService aiCallService;

    @Autowired
    WebSocketConfig(AiCallService aiCallService) {
        this.aiCallService = aiCallService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketServerHandlerClassicTicTacToe(), "/games/tictactoe").setAllowedOrigins("*");
        registry.addHandler(webSocketServerHandlerTicTacToeNFields(), "/games/tictactoenfields").setAllowedOrigins("*");
        registry.addHandler(webSocketServerHandlerSoccer(), "/games/soccer").setAllowedOrigins("*");
    }

    @Bean
    WebSocketServerHandler webSocketServerHandlerClassicTicTacToe() {
        return new WebSocketServerHandler(new GameFactoryClassicTicTacToe(), aiCallService);
    }

    @Bean
    WebSocketServerHandler webSocketServerHandlerTicTacToeNFields() {
        return new WebSocketServerHandler(new GameFactoryTicTacToeNFields(), aiCallService);
    }

    @Bean
    WebSocketServerHandler webSocketServerHandlerSoccer() {
        return new WebSocketServerHandler(new GameFactorySoccer(), aiCallService);
    }
}
