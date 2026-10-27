package dental.flow.api.application.command;

public record RegisterUserCommand(
        String name,
        String login,
        String password,
        Long clinicId,
        String accessLevel,
        Boolean active
) {
}
