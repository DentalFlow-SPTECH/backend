package dental.flow.api.boot.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {

    public BusinessException(Failures failures) {
        super(failures);
    }

    public BusinessException(Failures failures, String customMessage) {
        super(failures, customMessage);
    }

    public BusinessException(Failures failures, String customMessage, Throwable cause) {
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
