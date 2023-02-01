package com.knsoft.user.services;

import java.util.TimerTask;

public class UserCleanupTask extends TimerTask {
    private UserService userService;

    public UserCleanupTask(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run() {
        userService.cleanUpNotValidatedUsers();
    }
}