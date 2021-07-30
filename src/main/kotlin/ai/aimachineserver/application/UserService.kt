package ai.aimachineserver.application

import ai.aimachineserver.application.commands.CreateUserCommand
import ai.aimachineserver.application.dtos.UserDto
import ai.aimachineserver.domain.user.User
import ai.aimachineserver.domain.user.UserRepository
import ai.aimachineserver.domain.user.UserRole
import ai.aimachineserver.utils.SecurityUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val securityUtils: SecurityUtils
) {

    init {
        val adminUsername = System.getenv("DB_AIM_USER_ROLE_ADMIN_USERNAME")
        if (adminUsername != null && !userRepository.existsByUsername(adminUsername)) {
            val adminPassword = System.getenv("DB_AIM_USER_ROLE_ADMIN_PASSWORD")
            val adminUser = User(adminUsername, passwordEncoder.encode(adminPassword), UserRole.ADMIN.roleName)
            userRepository.save(adminUser)
        }
    }

    fun createUser(command: CreateUserCommand): UserDto? {
        val userAlreadyExists = userRepository.existsByUsername(command.username)
        return if (userAlreadyExists) {
            return null
        } else {
            val encodedPassword = passwordEncoder.encode(command.password)
            val newUser = User(command.username, encodedPassword)
            dto(userRepository.save(newUser))
        }
    }

    fun getSelf(): UserDto? {
        val userId = securityUtils.getLoggedUserId()
        return try {
            if (userId != null) {
                val user = userRepository.findUserById(userId)
                user?.let { dto(it) }
            } else {
                null
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }

    private fun dto(user: User) = UserDto(
        id = user.id,
        username = user.username,
        roles = user.authorities.map { it.authority }
    )
}
