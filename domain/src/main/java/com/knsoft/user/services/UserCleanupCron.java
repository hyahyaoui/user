package com.knsoft.user.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

/**
 * The {@code UserCleanupCron} class schedules the deletion of users who have not validated their registrations.
 * <p>This class uses a {@link Timer} to schedule the task with an interval specified by the
 * {@code registrationCheckLapseInMinutes} parameter. The deletion of unvalidated users is performed by
 * the {@code UserCleanupTask} class if the time passed since the creation of the user's registration exceeds
 * the {@code registrationInvalidationLapseInMinute}.</p>
 * <p>This class takes in a {@link UserRegistrationService} object, which provides operations for accessing user
 * registration information, and the {@code registrationCheckLapseInMinutes} and
 * {@code registrationInvalidationLapseInMinute} parameters. These parameters specify the interval at which the
 * cleanup task should run and the maximum time that a user should not validate their registration before it is deleted,
 * respectively.</p>
 * <p>When the {@link #start()} method is called, it schedules the user
 * cleanup task and logs the task scheduling information to the log.</p>
 *
 * @author KNsoft
 * @version 1.0
 * @since 1.0.0
 */
public class UserCleanupCron {
    private final UserRegistrationService userRegistrationService;
    private final long registrationCheckLapseInSeconds;
    private Timer timer = new Timer();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCleanupCron.class);

    /**
     * Constructs a new instance of the {@code UserCleanupCron} class.
     *
     * @param userRegistrationService         A {@link UserRegistrationService} object, which provides
     *                                        the operations to access user registration information.
     * @param registrationCheckLapseInSeconds The interval at which the cleanup task should run,
     *                                        specified in minutes.
     */
    public UserCleanupCron(UserRegistrationService userRegistrationService,
                           long registrationCheckLapseInSeconds) {
        if (registrationCheckLapseInSeconds < 1) {
            throw new IllegalArgumentException("registrationCheckLapseInSeconds should be at least one second");
        }
        this.userRegistrationService = userRegistrationService;
        this.registrationCheckLapseInSeconds = registrationCheckLapseInSeconds;
    }


    /**
     * Schedules the deletion of users who have not validated their registrations.
     */
    public void start() {
        try {
            LOGGER.info("Scheduling user cleanup task with interval of {} seconds", registrationCheckLapseInSeconds);

            timer.schedule(new UserCleanupTask(userRegistrationService,
                            userRegistrationService.getRegistrationInvalidationLapseInMinute()),
                    0L, registrationCheckLapseInSeconds);
            LOGGER.info("User cleanup task scheduled");
        } catch (Exception e) {
            LOGGER.error("Failed to schedule user cleanup task", e);
        }
    }

    public void stop() {
        try {
            LOGGER.info("Stopping user clean up cron");
            timer.cancel();
            LOGGER.info("User cleanup cron task cron stopped");
        } catch (Exception e) {
            LOGGER.error("Failed to stop user cleanup cron", e);
        }
    }

}