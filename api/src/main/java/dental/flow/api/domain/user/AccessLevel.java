package dental.flow.api.domain.user;

import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.boot.exception.Failures;

import java.util.Locale;

public enum AccessLevel {
    DENTISTA,
    RECEPCIONISTA;

    public static AccessLevel from(String value) {
        if (value == null || value.isBlank()) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nível de acesso é obrigatório.");
        }
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new DomainException(Failures.DOMAIN_ERROR, "O nível de acesso deve ser DENTISTA ou RECEPCIONISTA.");
        }
    }
}
