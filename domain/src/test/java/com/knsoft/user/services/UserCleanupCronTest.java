package com.knsoft.user.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.util.Timer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserCleanupCronTest {

    @Mock
    private UserRegistrationService userRegistrationService;

    @Mock
    private Logger logger;

    @Mock
    private Timer timer;

    private UserCleanupCron userCleanupCron;

    @Before
    public void setUp() {
        userCleanupCron = new UserCleanupCron(userRegistrationService, 1, 1);
    }

    @Test
    public void testStartSuccess() {
        when(logger.isInfoEnabled()).thenReturn(true);
        doNothing().when(timer).schedule(any(UserCleanupTask.class), anyLong(), anyLong());

        userCleanupCron.start();

        verify(logger).info("Scheduling user cleanup task with interval of 1 minutes");
        verify(logger).info("User cleanup task scheduled");
    }

    @Test
    public void testStartFailure() {
        when(logger.isInfoEnabled()).thenReturn(true);
        Exception exception = new Exception();
        doNothing().when(timer).schedule(any(UserCleanupTask.class), anyLong(), anyLong());

        userCleanupCron.start();

        verify(logger).info("Scheduling user cleanup task with interval of 1 minutes");
        verify(logger).error("Failed to schedule user cleanup task", exception);
    }
}
