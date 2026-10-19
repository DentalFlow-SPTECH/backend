package dental.flow.api.boot.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends ApiException {

    public DatabaseException(Failures failures) {
        super(failures);
    }

    public DatabaseException(Failures failures, String customMessage) {
        super(failures, customMessage);
    }

    public DatabaseException(Failures failures, String customMessage, Throwable cause) {
        super(failures, customMessage, cause);
    }

    @Override
    public String getCode() {
        return super.getCode();
    }

    @Override
    public HttpStatus getStatus() {
        return super.getStatus();
    }
}
