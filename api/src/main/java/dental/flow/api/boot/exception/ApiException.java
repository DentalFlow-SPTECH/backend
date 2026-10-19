package dental.flow.api.boot.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {

    private final String code;
    private final HttpStatus status;
    private final String message;

    protected ApiException(Failures failure) {
        this(failure, failure.getMessage(), null);
    }

    protected ApiException(Failures failure, String customMessage) {
        this(failure, customMessage, null);
    }

    protected ApiException(Failures failure, String customMessage, Throwable cause) {
        super(customMessage != null && !customMessage.isBlank() ? customMessage : failure.getMessage(), cause);
        this.code = failure.getCode();
        this.status = failure.getStatus();
        this.message = customMessage != null && !customMessage.isBlank() ? customMessage : failure.getMessage();
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
