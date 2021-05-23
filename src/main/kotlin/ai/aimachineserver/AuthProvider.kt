package ai.aimachineserver

import ai.aimachineserver.userlogin.UserRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component
import javax.security.auth.login.LoginException

@Component
class AuthProvider(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : AuthenticationProvider {
    override fun authenticate(auth: Authentication): Authentication? {
        return when {
            auth is PreAuthenticatedAuthenticationToken -> auth
            auth.isValid() -> PreAuthenticatedAuthenticationToken(auth.principal, auth.credentials)
            else -> throw LoginException()
        }
    }

    private fun Authentication.isValid(): Boolean {
        val user = userRepository.findByUsername(this.principal as String)
        return if (user != null) {
            passwordEncoder.matches(this.credentials as String, user.password)
        } else {
            false
        }
    }

    override fun supports(authentication: Class<*>?) = true
}