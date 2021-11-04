package ai.aimachineserver.domain.games.tictactoe;

import ai.aimachineserver.domain.games.AbstractGame;
import ai.aimachineserver.domain.games.KeyValue;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

public class GameTicTacToe extends AbstractGame {
    private static final Player playerStub = new PlayerHuman("", Symbol.SYMBOL_X);
    private final Board board;
    private final Judge judge;

    private Player player1 = playerStub;
    private Player player2 = playerStub;
    private Player currentPlayer = player1;
    private TurnResult turnResult = TurnResult.GAME_ONGOING;
    private int turnNumber = 0;

    public GameTicTacToe(Board board, Judge judge, String gameName) {
        super(gameName);
        this.board = board;
        this.judge = judge;
    }

    @Override
    public void onPlayerJoinedGame(WebSocketSession session) {
        playerSessions.add(session);
        if (player1 == playerStub) {
            player1 = new PlayerHuman(session.getId(), Symbol.SYMBOL_O);
            currentPlayer = player1;
        } else {
            player2 = new PlayerHuman(session.getId(), Symbol.SYMBOL_X);
            broadcastMessage(new KeyValue("eventType", "current_player"), new KeyValue("eventMessage", currentPlayer.name));
        }
        int playersCount = playerSessions.size();
        broadcastMessage(new KeyValue("eventType", "players_count"), new KeyValue("eventMessage", "$playersCount"));
        String message = playersCount == 1 ? "Waiting for opponent" : "Game has started";
        broadcastMessage(new KeyValue("eventType", "server_message"), new KeyValue("eventMessage", message));
    }

    @Override
    public void onFieldClicked(int rowIndex, int colIndex) {
        if (board.isFieldAvailable(rowIndex, colIndex)) {
            System.out.printf("Clicked [row, col]: [%d, %d]", rowIndex, colIndex);
            JSONObject json = new JSONObject();
            json.put("rowIndex", rowIndex);
            json.put("colIndex", colIndex);
            json.put("fieldToken", currentPlayer.symbol.token);
            String data = json.toString();
            broadcastMessage(new KeyValue("eventType", "new_move_to_mark"), new KeyValue("eventMessage", data));
            if (turnResult == TurnResult.GAME_ONGOING) {
                turnNumber++;
                currentPlayer.makeMove(board, rowIndex, colIndex);
                turnResult = judge.announceTurnResult(turnNumber);
                if (turnResult == TurnResult.GAME_ONGOING) {
                    currentPlayer = assignPlayer();
                    broadcastMessage(new KeyValue("eventType", "current_player"), new KeyValue("eventMessage", currentPlayer.name));
                } else {
                    String resultMessage = getResultMessage();
                    broadcastMessage(new KeyValue("eventType", "game_ended"), new KeyValue("eventMessage", resultMessage));
                    broadcastMessage(new KeyValue("eventType", "current_player"), new KeyValue("eventMessage", "none"));
                }
            }
        }
    }

    private Player assignPlayer() {
        if (currentPlayer == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    private String getResultMessage() {
        if (turnResult == TurnResult.TIE) {
            return "Tie";
        } else {
            return "${currentPlayer.symbol.identifier} won";
        }
    }

    @Override
    public void onDisconnect(WebSocketSession session) {
        playerSessions.remove(session);
        broadcastMessage(new KeyValue("eventType", "server_message"), new KeyValue("eventMessage", "Player disconnected"));
        broadcastMessage(new KeyValue("eventType", "server_message"), new KeyValue("eventMessage", playerSessions.size() + " players in game"));
    }
}
