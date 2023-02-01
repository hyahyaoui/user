package com.knsoft.user.services;

import java.util.Timer;

public class UserCleanupCron {

    private final UserService userService;
    private final long registrationCheckLapseInMinutes;

    public UserCleanupCron(UserService userService, long registrationCheckLapseInMinutes) {
        this.userService = userService;
        this.registrationCheckLapseInMinutes = registrationCheckLapseInMinutes;
    }

    public void start() {
        Timer timer = new Timer();
        timer.schedule(new UserCleanupTask(userService), 0, registrationCheckLapseInMinutes * 60 * 1000);
    }
}
