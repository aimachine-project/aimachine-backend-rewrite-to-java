package ai.aimachineserver.application;

import ai.aimachineserver.application.commands.CallAiCommand;
import ai.aimachineserver.domain.games.AbstractGame;
import ai.aimachineserver.domain.games.Game;
import ai.aimachineserver.domain.games.GameFactory;
import ai.aimachineserver.domain.games.KeyValue;
import ai.aimachineserver.utils.GeneralUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WebSocketServerHandler extends TextWebSocketHandler {
    private final GameFactory gameFactory;
    private final AiCallService aiCallService;
    private final Map<String, Game> games = new HashMap<>();
    private final Map<String, Game> aiGames = new HashMap<>();
    private int gamesCounter = 1;
    private int gamesVsAiCounter = 1;

    public WebSocketServerHandler(GameFactory gameFactory, AiCallService aiCallService) {
        this.gameFactory = gameFactory;
        this.aiCallService = aiCallService;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws IOException {
        System.out.println("Client ${session.id} connected");
        sendWebsocketMessage(session, new KeyValue("eventType", "client_id"), new KeyValue("eventMessage", session.getId()));
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> uriParams = GeneralUtils.parseQueryParams(uri.getQuery());
            switch (uriParams.get("gameType")) {
                case "HumanVsHuman":
                    handleHumanVsHumanGameRequest(session);
                    break;
                case "HumanVsAi":
                    handleHumanVsAiGameRequest(session);
                    break;
                case "AiVsHuman":
                    String gameId = uriParams.get("gameId");
                    if (gameId != null) {
                        handleAiVsHumanGameRequest(session, gameId);
                    } else {
                        System.out.println("No gameId found in URI");
                    }
                    break;
                default:
                    System.out.println("No gameType found in URI");
            }
        }
    }

    void sendWebsocketMessage(WebSocketSession session, KeyValue... keyValues) throws IOException {
        JSONObject json = new JSONObject();
        Arrays.stream(keyValues).forEach(pair ->
                json.put(pair.getKey(), pair.getValue())
        );
        new ConcurrentWebSocketSessionDecorator(session, 2000, 200)
                .sendMessage(new TextMessage(json.toString()));
    }

    private void handleHumanVsHumanGameRequest(WebSocketSession session) throws IOException {
        String gameId = "game" + gamesCounter;
        sendWebsocketMessage(session, new KeyValue("eventType", "game_id"), new KeyValue("eventMessage", gameId));
        if (games.containsKey(gameId)) {
            games.get(gameId).onPlayerJoinedGame(session);
            System.out.printf("player %s joined game %s", session.getId(), gameId);
            gamesCounter++;
        } else {
            Game game = gameFactory.createGame();
            game.onPlayerJoinedGame(session);
            games.put(gameId, game);
            System.out.println("games: " + games.keySet());
        }
    }

    private void handleHumanVsAiGameRequest(WebSocketSession session) throws IOException {
        String gameId = "game" + gamesVsAiCounter++;
        sendWebsocketMessage(session, new KeyValue("eventType", "game_id"), new KeyValue("eventMessage", gameId));
        AbstractGame game = gameFactory.createGame();
        game.onPlayerJoinedGame(session);
        aiGames.put(gameId, game);
        System.out.println("games: " + aiGames.keySet());
        System.out.println("calling AI to join the game...");
        aiCallService.callAi(new CallAiCommand(game.getGameName(), "AiVsHuman", gameId));
    }

    private void handleAiVsHumanGameRequest(WebSocketSession session, String gameId) throws IOException {
        sendWebsocketMessage(session, new KeyValue("eventType", "game_id"), new KeyValue("eventMessage", gameId));
        if (aiGames.containsKey(gameId)) {
            aiGames.get(gameId).onPlayerJoinedGame(session);
            System.out.printf("player %s joined game $gameId", session.getId());
        }
    }

    @Override
    public void handleTextMessage(@NotNull WebSocketSession session, TextMessage message) {
        JSONObject json = new JSONObject(message.getPayload());
        if ("make_move".equals(json.getString("eventType"))) {
            JSONObject data = json.getJSONObject("eventMessage");
            String gameId = data.getString("gameId");
            int rowIndex = data.getInt("rowIndex");
            int colIndex = data.getInt("colIndex");

            URI uri = session.getUri();
            if (uri != null) {
                Map<String, String> uriParams = GeneralUtils.parseQueryParams(uri.getQuery());
                switch (uriParams.get("gameType")) {
                    case "HumanVsHuman":
                        games.get(gameId).onFieldClicked(rowIndex, colIndex);
                        break;
                    case "HumanVsAi":
                        aiGames.get(gameId).onFieldClicked(rowIndex, colIndex);
                        break;
                    case "AiVsHuman":
                        aiGames.get(gameId).onFieldClicked(rowIndex, colIndex);
                    default:
                        System.out.println("No gameType found in URI");
                }
            }
        } else {
            System.out.println("client event type not handled");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) {
        System.out.printf("Client %s disconnected", session.getId());
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> uriParams = GeneralUtils.parseQueryParams(uri.getQuery());
            switch (uriParams.get("gameType")) {
                case "HumanVsHuman":
                    disbandGame(session, games);
                    break;
                case "HumanVsAi":
                case "AiVsHuman":
                    disbandGame(session, aiGames);
                    break;
                default:
                    System.out.println("No gameType found in URI");
            }
        }
    }

    private void disbandGame(WebSocketSession session, Map<String, Game> games) {
        Map.Entry<String, Game> disbandingGameEntry = games.entrySet().stream().filter(it -> it.getValue().getAllPlayerSessions().contains(session)).findAny().orElse(null);
        if (disbandingGameEntry == null) return;
        String gameId = disbandingGameEntry.getKey();
        Game game = disbandingGameEntry.getValue();
        game.onDisconnect(session);
        games.remove(gameId);
        String serverMessage = "Game has been disbanded. Restart client to play a new game";
        game.broadcastMessage(new KeyValue("eventType", "game_disbanded"), new KeyValue("eventMessage", serverMessage));
        System.out.println("Ongoing games: ${games.keys}");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        System.out.printf("An error has occurred at websocket session: %s", session.getId());
        System.out.printf("Exception message: %s", exception.getMessage());
    }
}
