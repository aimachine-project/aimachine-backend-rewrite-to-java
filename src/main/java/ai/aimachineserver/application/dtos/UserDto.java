package ai.aimachineserver.application.dtos;

import java.util.List;

public class UserDto {
    public final Long id;
    public final String username;
    public final List<String> roles;

    public UserDto(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
