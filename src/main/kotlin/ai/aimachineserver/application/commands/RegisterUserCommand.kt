package ai.aimachineserver.application.commands

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@NoArgsConstructor
@AllArgsConstructor
class RegisterUserCommand(
    @NotNull(message = "username cannot be null")
    @Size(min = 1, max = 100)
    val username: String,

    @NotNull(message = "password cannot be null")
    @Size(min = 1, max = 100)
    val password: String
)
