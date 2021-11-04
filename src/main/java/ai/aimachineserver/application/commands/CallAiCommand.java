package ai.aimachineserver.application.commands;

public class CallAiCommand {
    public String gameName;
    public String requestedGameType;
    public String gameId;

    public CallAiCommand(String gameName, String requestedGameType, String gameId) {
        this.gameName = gameName;
        this.requestedGameType = requestedGameType;
        this.gameId = gameId;
    }
}
