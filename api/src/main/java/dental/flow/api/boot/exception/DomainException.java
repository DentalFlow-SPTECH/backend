
package dental.flow.api.boot.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends ApiException {

    public DomainException(Failures failure) {
        super(failure);
    }

    public DomainException(Failures failure, String customMessage) {
        super(failure, customMessage);
    }

    public DomainException(Failures failure, String customMessage, Throwable cause) {
        super(failure, customMessage, cause);
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
