package ai.aimachineserver.application.commands;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class CreateUserCommand {
    @NotNull(message = "username cannot be null")
    @Size(min = 1, max = 100)
    public String username;

    @NotNull(message = "password cannot be null")
    @Size(min = 1, max = 100)
    public String password;
}
