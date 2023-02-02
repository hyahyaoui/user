package com.knsoft.user.services;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

public class UserCleanupTaskTest {

    private UserRegistrationService userRegistrationService;
    private UserCleanupTask userCleanupTask;
    private Logger logger;

    @Before
    public void setUp() {
        userRegistrationService = mock(UserRegistrationService.class);
        logger = mock(Logger.class);
        when(LoggerFactory.getLogger(UserCleanupTask.class)).thenReturn(logger);
        userCleanupTask = new UserCleanupTask(userRegistrationService, 1000L);
    }

    @Test
    public void run_success() {
        userCleanupTask.run();
        verify(userRegistrationService).invalidateExpiredRegistrations(1000L);
        verify(logger).info("User cleanup task started");
        verify(logger).info("User cleanup task completed");
    }

    @Test
    public void run_failure() {
        Exception exception = new Exception("Test exception");
        doThrow(exception).when(userRegistrationService).invalidateExpiredRegistrations(1000L);
        userCleanupTask.run();
        verify(userRegistrationService).invalidateExpiredRegistrations(1000L);
        verify(logger).info("User cleanup task started");
        verify(logger).error("User cleanup task failed", exception);
    }
}
