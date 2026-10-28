package dental.flow.api.adapters.rest.in.controller.dto.request;

public record RegisterUserRequestDTO(
        String name,
        String login,
        String password,
        Long clinicId,
        Long dentistId,
        String accessLevel,
        Boolean active
) {
    public RegisterUserRequestDTO(String name, String login, String password, Long clinicId,
                                  String accessLevel, Boolean active) {
        this(name, login, password, clinicId, null, accessLevel, active);
    }
}
