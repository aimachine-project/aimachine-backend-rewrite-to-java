package ai.aimachineserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${custom.db-user-admin-username}}")
    public String dbUserAdminUsername;

    @Value("${custom.db-user-admin-password}")
    public String dbUserAdminPassword;

    @Value("${custom.ai-service-url}")
    public String aiServiceUrl;
}
