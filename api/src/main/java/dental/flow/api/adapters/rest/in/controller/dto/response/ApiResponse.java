package dental.flow.api.adapters.rest.in.controller.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ApiResponse<T>(
        int status,
        String message,
        T data,
        String timestamp
) {

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data, now());
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(200, message, data, now());
    }

    public static <T> ApiResponse<T> noContent(String message) {
        return new ApiResponse<>(204, message, null, now());
    }

    private static String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
