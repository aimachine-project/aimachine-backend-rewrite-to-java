package ai.aimachineserver

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthEntryPointImpl: BasicAuthenticationEntryPoint() {
    override fun afterPropertiesSet() {
        this.realmName = "aimachine-backend"
        super.afterPropertiesSet()
    }
}