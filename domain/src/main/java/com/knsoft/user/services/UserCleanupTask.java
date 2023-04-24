package com.knsoft.user.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * The {@code UserCleanupTask} class extends {@link java.util.TimerTask} and is used to delete users who
 * have not validated their registrations.
 *
 * @author KnSoft
 * @version 1.0
 * @see java.util.TimerTask
 * @since 1.0
 */
public class UserCleanupTask extends TimerTask {
    private final UserRegistrationService userRegistrationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCleanupTask.class);
    private final long registrationExpirationInSeconds;

    /**
     * Constructs a new {@code UserCleanupTask} object and sets the provided {@link UserRegistrationService} and the lapsed time in milliseconds.
     *
     * @param userRegistrationService the service used to delete users who have not validated their registrations
     * @param registrationExpirationInSeconds the amount of time in seconds after which an unvalidated registration should be considered expired
     */
    public UserCleanupTask(UserRegistrationService userRegistrationService, long registrationExpirationInSeconds) {
        this.userRegistrationService = userRegistrationService;
        this.registrationExpirationInSeconds = registrationExpirationInSeconds;
    }

    /**
     * This method is executed when the {@code UserCleanupTask} is scheduled.
     * <p>
     * It logs the start of the cleanup task and calls the {@link UserRegistrationService#invalidateExpiredRegistrations(long)}
     * method with the provided lapsed time in milliseconds. If an {@link Exception} occurs, it is logged as an error.
     */
    @Override
    public void run() {
        try {
            LOGGER.info("User cleanup started");
            userRegistrationService.invalidateExpiredRegistrations(registrationExpirationInSeconds);
            LOGGER.info("User cleanup completed");
        } catch (Exception e) {
            LOGGER.error("User cleanup task failed", e);
        }
    }
}