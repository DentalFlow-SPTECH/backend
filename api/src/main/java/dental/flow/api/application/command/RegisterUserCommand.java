package dental.flow.api.application.command;

public record RegisterUserCommand(
        String name,
        String login,
        String password,
        Long clinicId,
        Long dentistId,
        String accessLevel,
        Boolean active
) {
    public RegisterUserCommand(String name, String login, String password, Long clinicId,
                               String accessLevel, Boolean active) {
        this(name, login, password, clinicId, null, accessLevel, active);
    }
}
