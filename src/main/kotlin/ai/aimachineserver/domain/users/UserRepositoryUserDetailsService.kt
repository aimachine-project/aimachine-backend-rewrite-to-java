package ai.aimachineserver.domain.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserRepositoryUserDetailsService
@Autowired constructor(
    private val userRepo: UserRepository
) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByUsername(username)
        if (user != null) {
            return user
        }
        throw UsernameNotFoundException(
            "User '$username' has not been found."
        )
    }
}
