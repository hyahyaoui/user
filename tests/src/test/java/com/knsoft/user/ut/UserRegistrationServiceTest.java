package com.knsoft.user.ut;

import com.knsoft.commons.exceptions.EntityNotFoundException;
import com.knsoft.user.model.User;
import com.knsoft.user.model.UserRegistrationRequest;
import com.knsoft.user.repositories.UserRegistrationRequestRepository;
import com.knsoft.user.services.UserRegistrationService;
import com.knsoft.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserRegistrationServiceTest {

    private UserRegistrationService userRegistrationService;
    private UserRegistrationRequestRepository userRegistrationRequestRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRegistrationRequestRepository = mock(UserRegistrationRequestRepository.class);
        userService = mock(UserService.class);
        userRegistrationService = new UserRegistrationService(userRegistrationRequestRepository, userService, 5);
    }

    @Test
    void registerUser() {
        // Given
        User user = new User();
        user.setUserName("user");
        user.setEmail("user@company.com");

        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUserUid(user.getUid());
        userRegistrationRequest.setRegistrationRequestDate(Instant.now().toEpochMilli());

        when(userService.create(user)).thenReturn(user);
        when(userRegistrationRequestRepository.create(ArgumentMatchers.any(UserRegistrationRequest.class)))
                .thenReturn(userRegistrationRequest);

        // When
        User registeredUser = userRegistrationService.register(user);

        // Then
        verify(userService).create(user);
        verify(userRegistrationRequestRepository).create(userRegistrationRequest);
    }

    @Test
    void validateRegistration() {
        // Given
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUserUid("456");
        userRegistrationRequest.setRegistrationRequestDate(Instant.now().toEpochMilli());

        User user = new User();
        user.setUserName("user");
        user.setEmail("user@company.com");
        user.setStatus(User.Status.INACTIVE);

        when(userRegistrationRequestRepository.findRegistrationRequestById(userRegistrationRequest.getUid()))
                .thenReturn(Optional.of(userRegistrationRequest));
        when(userService.findUserByUid(user.getUid())).thenReturn(Optional.of(user));

        // When
        userRegistrationService.validateRegistration(userRegistrationRequest.getUid());

        // Then
        verify(userService).update(user.getUid(), user);
        verify(userRegistrationRequestRepository).delete(userRegistrationRequest.getUid());
    }

    @Test
    void validateRegistrationThrowsExceptionWhenRequestNotFound() {
        // Given
        String registrationId = "123";
        when(userRegistrationRequestRepository.findRegistrationRequestById(registrationId)).thenReturn(Optional.empty());

        // When / Then
        EntityNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userRegistrationService.validateRegistration(registrationId);
        });
        org.junit.jupiter.api.Assertions.assertEquals("User registration not found with ID " + registrationId, exception.getMessage());
    }

    @Test
    void invalidateRegistration() {
        // Given
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUserUid("456");

        User user = new User();

        when(userRegistrationRequestRepository.findRegistrationRequestById(userRegistrationRequest.getUid()))
                .thenReturn(Optional.of(userRegistrationRequest));
        when(userService.findUserByUid(user.getUid())).thenReturn(Optional.of(user));

        // When
       // userRegistrationService.invalidateRegistration(userRegistrationRequest.getUid());

        // Then
        verify(userService).delete(user.getUid());
        verify(userRegistrationRequestRepository).delete(userRegistrationRequest.getUid());
    }

    @Test
    void invalidateExpiredRegistrations() {
        // Given
        long creationDateThreshold = System.currentTimeMillis() - 1000;
        UserRegistrationRequest userRegistrationRequest1 = new UserRegistrationRequest();
        userRegistrationRequest1.setUserUid("456");
        userRegistrationRequest1.setRegistrationRequestDate(creationDateThreshold - 1000);

        UserRegistrationRequest userRegistrationRequest2 = new UserRegistrationRequest();
        userRegistrationRequest2.setUserUid("567");
        userRegistrationRequest2.setRegistrationRequestDate(creationDateThreshold - 1000);

        UserRegistrationRequest userRegistrationRequest3 = new UserRegistrationRequest();
        userRegistrationRequest3.setUserUid("678");
        userRegistrationRequest3.setRegistrationRequestDate(creationDateThreshold + 1000);

        when(userRegistrationRequestRepository.findRegistrationBefore(creationDateThreshold))
                .thenReturn(List.of(userRegistrationRequest1, userRegistrationRequest2, userRegistrationRequest3));

        // When
        userRegistrationService.invalidateExpiredRegistrations(1000);

        // Then
        verify(userService, times(2)).delete(anyString());
        verify(userRegistrationRequestRepository, times(2)).delete(anyString());
    }
}