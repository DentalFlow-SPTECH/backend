
package dental.flow.api.boot.exception;

import org.springframework.http.HttpStatus;

public enum Failures {

    DOMAIN_ERROR(
            "DMN001",
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Violação de domínio"
    ),
    DATABASE_ERROR(
            "DTBS001",
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro de banco de dados"
    ),
    BUSINESS_FAILURE(
            "BSN001",
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Violação de regra de negócio"
    ),
    NOT_FOUND(
            "NFD001",
            HttpStatus.NOT_FOUND,
            "Recurso não encontrado"
    );

    private final String code;
    private final HttpStatus status;
    private final String message;

    Failures(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCodigo() {
        return code;
    }

    public String getMensagem() {
        return message;
    }
}
