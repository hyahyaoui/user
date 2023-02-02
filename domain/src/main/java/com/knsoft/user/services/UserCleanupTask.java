package com.knsoft.user.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * TimerTask for deleting users who have not validated their registrations.
 */

/**
 * TimerTask for deleting users who have not validated their registrations.
 */
public class UserCleanupTask extends TimerTask {

    private final UserRegistrationService userRegistrationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCleanupTask.class);
    private final long lapseTimeInMilliseconds;


    public UserCleanupTask( UserRegistrationService userRegistrationService,
                           long lapseTimeInMilliseconds) {
        this.userRegistrationService = userRegistrationService;
        this.lapseTimeInMilliseconds = lapseTimeInMilliseconds;
    }


    @Override
    public void run() {
        try {
            LOGGER.info("User cleanup task started");
            userRegistrationService.invalidateExpiredRegistrations(lapseTimeInMilliseconds);
            LOGGER.info("User cleanup task completed");
        } catch (Exception e) {
            LOGGER.error("User cleanup task failed", e);
        }
    }
}