package ai.aimachineserver.application.dtos

class UserDto(
    private val id: Long?,
    private val username: String,
    private val roles: List<String>
)