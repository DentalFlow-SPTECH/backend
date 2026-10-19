package dental.flow.api.application.command;

public record RegisterUserCommand (
    String name,
    String email,
    String password,
    Long clinicId
){}
