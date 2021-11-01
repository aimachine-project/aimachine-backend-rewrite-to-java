package ai.aimachineserver.domain.user;

public enum UserRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    public String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }
}
