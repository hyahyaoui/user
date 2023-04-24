package user.services;

import com.knsoft.user.services.UserCleanupCron;
import com.knsoft.user.services.UserCleanupTask;
import com.knsoft.user.services.UserRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Timer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserCleanupCronTest {
    private UserRegistrationService userRegistrationService;
    private UserCleanupCron userCleanupCron;

    @BeforeEach
    public void setUp() {
        userRegistrationService = mock(UserRegistrationService.class);
        userCleanupCron = new UserCleanupCron(userRegistrationService, 2);

    }

    @Test
    public void testConstructorWithNegativeCheckLapse() {
        assertThrows(IllegalArgumentException.class, () -> new UserCleanupCron(userRegistrationService, -1L));
    }

    @Test
    public void testConstructorWithZeroCheckLapse() {
        assertThrows(IllegalArgumentException.class, () -> new UserCleanupCron(userRegistrationService, 0L));
    }

    @Test
    public void testStart() throws Exception {
        // Access and modify the private Timer field
        Field timerField = UserCleanupCron.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        Timer timerMock = mock(Timer.class);
        timerField.set(userCleanupCron, timerMock);

        // Test the start() method
        when(userRegistrationService.getRegistrationInvalidationLapseInMinute()).thenReturn(10L);
        doNothing().when(timerMock).schedule(any(UserCleanupTask.class), eq(0L), eq(2L));
        userCleanupCron.start();
        verify(userRegistrationService).getRegistrationInvalidationLapseInMinute();
        verify(timerMock).schedule(any(UserCleanupTask.class), eq(0L), eq(2L));
    }

    @Test
    public void testStop() throws Exception {
        // Access and modify the private Timer field
        Field timerField = UserCleanupCron.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        Timer timerMock = mock(Timer.class);
        timerField.set(userCleanupCron, timerMock);

        // Test the stop() method
        doNothing().when(timerMock).cancel();
        userCleanupCron.stop();
        verify(timerMock).cancel();
    }
}
