package dental.flow.api.application.command;

public record RegisterDentistCommand(
        Long clinicId,
        String name,
        String cro,
        String specialty,
        String phone
) {
}
