package dental.flow.api.application.service;

import dental.flow.api.application.command.RegisterUserCommand;
import dental.flow.api.application.out.RegisterUserRepository;
import dental.flow.api.application.out.RegisterDentistRepository;
import dental.flow.api.boot.exception.BusinessException;
import dental.flow.api.boot.exception.DomainException;
import dental.flow.api.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {
    @Mock private RegisterUserRepository repository;
    @Mock private RegisterDentistRepository dentistRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private RegisterUserService service;

    @Test
    void shouldRegisterUserWithBcryptPassword() {
        RegisterUserCommand command = new RegisterUserCommand(" Ana ", "ANA", "123456", 1L, "recepcionista", null);
        when(repository.existsByLogin(any())).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hash");
        when(repository.register(any())).thenReturn("User id:1 registered successfully");

        assertEquals("User id:1 registered successfully", service.register(command).message());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository).register(captor.capture());
        assertEquals("ana", captor.getValue().getLogin());
        assertEquals("RECEPCIONISTA", captor.getValue().getAccessLevel());
        assertTrue(captor.getValue().getActive());
        assertEquals("hash", captor.getValue().getPassword());
    }

    @Test
    void shouldRejectDuplicateLogin() {
        RegisterUserCommand command = new RegisterUserCommand("Ana", "ana", "123456", 1L, "RECEPCIONISTA", true);
        when(repository.existsByLogin(any())).thenReturn(true);
        assertThrows(BusinessException.class, () -> service.register(command));
        verify(repository, never()).register(any());
    }

    @Test
    void shouldRejectInvalidUserBeforeRepositoryCall() {
        RegisterUserCommand command = new RegisterUserCommand("Ana", "ana", "123", 1L, "RECEPCIONISTA", true);
        assertThrows(DomainException.class, () -> service.register(command));
        verify(repository, never()).existsByLogin(any());
    }
}
