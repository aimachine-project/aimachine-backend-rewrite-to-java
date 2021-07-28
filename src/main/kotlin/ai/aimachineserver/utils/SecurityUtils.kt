package ai.aimachineserver.utils

import ai.aimachineserver.domain.user.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityUtils {
    fun getLoggedUserId(): Long? {
        val loggedUser = SecurityContextHolder.getContext().authentication.principal as User
        return loggedUser.id
    }
}