package ai.aimachineserver.application.commands

class CallAiCommand(
    val gameName: String,
    val requestedGameType: String,
    val gameId: String
) {
    private constructor() : this("", "", "")
}
