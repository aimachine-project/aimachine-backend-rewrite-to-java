package ai.aimachineserver.application

import ai.aimachineserver.config.AppConfig
import ai.aimachineserver.application.commands.CallAiCommand
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AiCallService(
    private val restTemplate: RestTemplate,
    private val appConfig: AppConfig
) {
    fun callAi(command: CallAiCommand): ResponseEntity<String> {
        val requestUrl =
            "${appConfig.aiServiceUrl}/${command.gameName}?requestedGameType=${command.requestedGameType}&gameId=${command.gameId}"
        return restTemplate.getForEntity(requestUrl, String::class.java)
    }
}
