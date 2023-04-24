package user.services;

import com.knsoft.user.services.UserCleanupTask;
import com.knsoft.user.services.UserRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class UserCleanupTaskTest {

    private UserCleanupTask userCleanupTask;

    @Mock
    private UserRegistrationService userRegistrationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userCleanupTask = new UserCleanupTask(userRegistrationService, 60L);
    }

    @Test
    public void testRun_UserCleanupTaskCompletedSuccessfully() {
        // Act
        userCleanupTask.run();

        // Assert
        verify(userRegistrationService, times(1)).invalidateExpiredRegistrations(60L);
    }

    @Test
    public void testRun_UserCleanupTaskThrowsException() {
        // Arrange
        doThrow(RuntimeException.class).when(userRegistrationService).invalidateExpiredRegistrations(anyLong());

        // Act
        userCleanupTask.run();

        // Assert
        // No interactions with logger are verified as per your requirement
    }

    @Test
    public void testRun_LoggerNotEnabled() {
        // Act
        userCleanupTask.run();

        // Assert
        verify(userRegistrationService, times(1)).invalidateExpiredRegistrations(60L);
    }
}
