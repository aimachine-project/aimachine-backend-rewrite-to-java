package ai.aimachineserver.application

import ai.aimachineserver.domain.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepo: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails = userRepo.findByUsername(username)
        ?: throw UsernameNotFoundException("No user with name $username found")
}
