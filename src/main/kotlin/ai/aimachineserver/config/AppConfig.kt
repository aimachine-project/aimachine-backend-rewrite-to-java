package ai.aimachineserver.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Value("\${custom.db-user-admin-username}")
    lateinit var dbUserAdminUsername: String

    @Value("\${custom.db-user-admin-password}")
    lateinit var dbUserAdminPassword: String

    @Value("\${custom.ai-service-url}")
    lateinit var aiServiceUrl: String
}
