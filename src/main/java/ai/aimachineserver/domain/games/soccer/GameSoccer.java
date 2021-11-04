package ai.aimachineserver.domain.games.soccer;

import ai.aimachineserver.domain.games.AbstractGame;
import ai.aimachineserver.domain.games.KeyValue;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

import static ai.aimachineserver.domain.games.soccer.TurnResultSoccer.TURN_ONGOING;

public class GameSoccer extends AbstractGame {
    private static final PlayerSoccer playerStub = new PlayerSoccerHuman("");
    private final BoardSoccer board;
    private final JudgeSoccer judge;

    private PlayerSoccer player1 = playerStub;
    private PlayerSoccer player2 = playerStub;
    private PlayerSoccer currentPlayer = player1;
    private TurnResultSoccer turnResult = TURN_ONGOING;

    public GameSoccer(BoardSoccer board, JudgeSoccer judge, String gameName) {
        super(gameName);
        this.board = board;
        this.judge = judge;
    }

    @Override
    public void onPlayerJoinedGame(WebSocketSession session) {
        this.playerSessions.add(session);
        if (player1 == playerStub) {
            player1 = new PlayerSoccerHuman(session.getId());
            currentPlayer = player1;
        } else {
            player2 = new PlayerSoccerHuman(session.getId());
            broadcastMessage(new KeyValue("eventType", "current_player"), new KeyValue("eventMessage", currentPlayer.getName()));
            JSONObject json = new JSONObject();
            json.put("player1", player1.getName());
            json.put("player2", player2.getName());
            String data = json.toString();
            broadcastMessage(new KeyValue("eventType", "players_in_game"), new KeyValue("eventMessage", data));
            broadcastMessage(new KeyValue("eventType", "game_started"), new KeyValue("eventMessage", "game starting"));
        }
        int playersCount = playerSessions.size();
        broadcastMessage(new KeyValue("eventType", "players_count"), new KeyValue("eventMessage", "$playersCount"));
        String message = playersCount == 1 ? "Waiting for opponent" : "Game has started";
        broadcastMessage(new KeyValue("eventType", "server_message"), new KeyValue("eventMessage", message));
    }

    @Override
    public void onFieldClicked(int rowIndex, int colIndex) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            System.out.printf("%s clicked [row, col]: [%d, %d]", currentPlayer == player1 ? "player1" : "player2", rowIndex, colIndex);
            JSONObject json = new JSONObject();
            json.put("rowIndex", rowIndex);
            json.put("colIndex", colIndex);
            String data = json.toString();
            broadcastMessage(new KeyValue("eventType", "new_move_to_mark"), new KeyValue("eventMessage", data));
            currentPlayer.makeMove(board, rowIndex, colIndex);
            turnResult = judge.announceTurnResult();
            switch (turnResult) {
                case TURN_OVER:
                    currentPlayer = assignPlayer();
                    broadcastMessage(
                            new KeyValue("eventType", "current_player"),
                            new KeyValue("eventMessage", currentPlayer.getName())
                    );
                    break;
                case TURN_ONGOING:
                    broadcastMessage(
                            new KeyValue("eventType", "current_player"),
                            new KeyValue("eventMessage", currentPlayer.getName())
                    );
                    break;
                default:
                    String resultMessage = getEndgameMessage();
                    broadcastMessage(new KeyValue("eventType", "game_ended"), new KeyValue("eventMessage", resultMessage));
                    System.out.println(resultMessage);
                    broadcastMessage(
                            new KeyValue("eventType", "current_player"),
                            new KeyValue("eventMessage", "none")
                    );
            }
        }
    }

    private PlayerSoccer assignPlayer() {
        if (currentPlayer == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    private String getEndgameMessage() {
        switch (turnResult) {
            case WIN:
                return formatMessage(currentPlayer == player1);
            case LOSE:
                return formatMessage(currentPlayer != player1);
            default:
                return "Unexpected state, everyone is a winner";
        }
    }

    private String formatMessage(Boolean condition) {
        return String.format("%s won", condition ? "player1 " + player1.getName() : "player2 " + player2.getName());
    }

    @Override
    public void onDisconnect(WebSocketSession session) {
        try {
            playerSessions.remove(session);
            broadcastMessage(
                    new KeyValue("eventType", "server_message"),
                    new KeyValue("eventMessage", "Player disconnected")
            );
            broadcastMessage(
                    new KeyValue("eventType", "server_message"),
                    new KeyValue("eventMessage", "${playerSessions.count()} players in game")
            );
        } catch (NullPointerException e) {
            System.out.println("No client ref. Both clients already disconnected. Cannot broadcast message to missing socket");
        } catch (IllegalStateException e) {
            System.out.println("Illegal state. Both clients already disconnected. Cannot broadcast message to missing socket");
        }
    }
}
