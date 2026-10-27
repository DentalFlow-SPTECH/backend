package dental.flow.api.application.command;

public record RegisterClinicCommand(
        String name,
        String cnpj,
        String address,
        String phone
) {
}
