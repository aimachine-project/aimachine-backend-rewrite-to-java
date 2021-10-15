package ai.aimachineserver.application

import ai.aimachineserver.application.commands.CallAiCommand
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AiCallService(private val restTemplate: RestTemplate) {
    private companion object {
        const val AI_URL = "http://ai:8081"
    }

    fun callAi(command: CallAiCommand): ResponseEntity<String> {
        val requestUrl =
            "$AI_URL/${command.gameName}?requestedGameType=${command.requestedGameType}&gameId=${command.gameId}"
        return restTemplate.getForEntity(requestUrl, String::class.java)
    }
}
