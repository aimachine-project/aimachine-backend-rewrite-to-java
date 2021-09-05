package ai.aimachineserver.domain.games.tictactie

enum class Symbol(val identifier: String, val token: Int) {
    SYMBOL_X("X", 1),
    SYMBOL_O("O", -1)
}
