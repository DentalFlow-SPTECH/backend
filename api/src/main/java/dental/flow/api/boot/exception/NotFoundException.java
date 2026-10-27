package dental.flow.api.boot.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(Failures failures) {
        super(failures);
    }

    public NotFoundException(Failures failures, String customMessage) {
        super(failures, customMessage);
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
