package ai.aimachineserver.application.dtos

class UserDto(
    val id: Long?,
    val username: String,
    val roles: List<String>
)