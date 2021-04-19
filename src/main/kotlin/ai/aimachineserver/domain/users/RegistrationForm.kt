package ai.aimachineserver.domain.users

import lombok.Data
import org.springframework.security.crypto.password.PasswordEncoder

@Data
class RegistrationForm(
    private var username: String,
    private var password: String
) {

    fun toUser(passwordEncoder: PasswordEncoder): User {
        return User(username, passwordEncoder.encode(password))
    }
}
