package ai.aimachineserver.application;

import ai.aimachineserver.application.commands.CallAiCommand;
import ai.aimachineserver.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiCallService {
    private final RestTemplate restTemplate;
    private final AppConfig appConfig;

    @Autowired
    public AiCallService(RestTemplate restTemplate, AppConfig appConfig) {
        this.restTemplate = restTemplate;
        this.appConfig = appConfig;
    }

    public void callAi(CallAiCommand command) {
        String requestUrl = String.format("%s/%s?requestedGameType=%s&gameId=%s",
                appConfig.aiServiceUrl,
                command.gameName,
                command.requestedGameType,
                command.gameId
        );
        ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        if (response.getStatusCode() != HttpStatus.CREATED) {
            System.out.println("Unexpected status code while calling AI");
        }
    }
}

