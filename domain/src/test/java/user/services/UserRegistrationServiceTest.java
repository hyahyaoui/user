package user.services;

import com.knsoft.commons.data.exceptions.EntityNotFoundException;
import com.knsoft.user.exceptions.RegistrationRequestExpiredException;
import com.knsoft.user.model.User;
import com.knsoft.user.model.UserRegistrationRequest;
import com.knsoft.user.repositories.UserRegistrationRequestRepository;
import com.knsoft.user.services.UserRegistrationService;
import com.knsoft.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserRegistrationService")
class UserRegistrationServiceTest {

    private static final long REGISTRATION_INVALIDATION_LAPSE_IN_MINUTE = 30L;
    private static final String USER_UID = "userUid";
    private static final String REGISTRATION_ID = "registrationId";

    @Mock
    private UserRegistrationRequestRepository userRegistrationRequestRepository;

    @Mock
    private UserService userService;

    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRegistrationService = new UserRegistrationService(userRegistrationRequestRepository,
                userService,
                REGISTRATION_INVALIDATION_LAPSE_IN_MINUTE);
    }

    @Test
    @DisplayName("should register a new user")
    void should_register_new_user() {
        // given
        User user = new User();
        user.setStatus(null);
        when(userService.create(user)).thenReturn(user);

        // when
        User result = userRegistrationService.register(user);

        // then
        assertNotNull(result.getUid());
        assertEquals(User.Status.INACTIVE, result.getStatus());
        verify(userService, times(1)).create(user);
    }

    @Test
    @DisplayName("should validate registration")
    void should_validate_registration() {
        // given
        User user = new User(USER_UID);
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(REGISTRATION_ID);
        userRegistrationRequest.setUserUid(user.getUid());
        userRegistrationRequest.setRegistrationRequestDate(Instant.now().toEpochMilli());

        when(userRegistrationRequestRepository.findRegistrationRequestById(REGISTRATION_ID)).thenReturn(Optional.of(userRegistrationRequest));
        when(userService.findUserByUid(USER_UID)).thenReturn(Optional.of(user));

        // when
        userRegistrationService.validateRegistration(REGISTRATION_ID);

        // then
        verify(userRegistrationRequestRepository, times(1)).delete(REGISTRATION_ID);
        verify(userService, times(1)).activateUser(user.getUid());
    }

    @Test
    @DisplayName("should throw EntityNotFoundException when validation registration with wrong id")
    void should_throw_EntityNotFoundException_when_validation_registration_with_wrong_id() {
        // given
        String wrongRegistrationId = "wrongRegistrationId";

        // when
        when(userRegistrationRequestRepository.findRegistrationRequestById(wrongRegistrationId)).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class,
                () -> userRegistrationService.validateRegistration(wrongRegistrationId));

    }

    @Test
    @DisplayName("should throw RegistrationRequestExpiredException when validating an expired registration")
    void should_throw_RegistrationRequestExpiredException_when_validating_expired_registration() {
        // given
        User user = new User(USER_UID);
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
        userRegistrationRequest.setUserUid(user.getUid());
        userRegistrationRequest.setRegistrationRequestDate(Instant.now().toEpochMilli() - 200000000L);

        // when
        when(userRegistrationRequestRepository.findRegistrationRequestById(REGISTRATION_ID)).thenReturn(Optional.of(userRegistrationRequest));
        when(userService.findUserByUid(USER_UID)).thenReturn(Optional.of(user));

        // then
        assertThrows(RegistrationRequestExpiredException.class,
                () -> userRegistrationService.validateRegistration(REGISTRATION_ID));
    }


    @Test
    public void invalidateExpiredRegistrationsTest() {
        // Given
        long lapseTimeInMilliseconds = 86400000L; // 24 hours

        UserRegistrationRequest registrationRequest1 = new UserRegistrationRequest(REGISTRATION_ID + "_1");
        registrationRequest1.setRegistrationRequestDate(Instant.now().minusSeconds(86410).toEpochMilli());
        registrationRequest1.setUserUid(USER_UID + "_1");

        UserRegistrationRequest registrationRequest2 = new UserRegistrationRequest(REGISTRATION_ID + "_2");
        registrationRequest2.setRegistrationRequestDate(Instant.now().minusSeconds(86420).toEpochMilli());
        registrationRequest2.setUserUid(USER_UID + "_2");

        List<UserRegistrationRequest> expiredRegistrations = Arrays.asList(registrationRequest1, registrationRequest2);

        when(userRegistrationRequestRepository.findRegistrationRequestBefore(anyLong()))
                .thenReturn(expiredRegistrations);

        when(userRegistrationRequestRepository.findRegistrationRequestById(REGISTRATION_ID + "_1"))
                .thenReturn(Optional.of(registrationRequest1));
        when(userRegistrationRequestRepository.findRegistrationRequestById(REGISTRATION_ID + "_2"))
                .thenReturn(Optional.of(registrationRequest2));

        when(userService.findUserByUid(USER_UID + "_1"))
                .thenReturn(Optional.of(new User()));
        when(userService.findUserByUid(USER_UID + "_2"))
                .thenReturn(Optional.of(new User()));

        // Act
        userRegistrationService.invalidateExpiredRegistrations(lapseTimeInMilliseconds);

        // Assert
        verify(userRegistrationRequestRepository, times(1)).delete(registrationRequest1.getUid());
        verify(userRegistrationRequestRepository, times(1)).delete(registrationRequest2.getUid());
        verify(userService, times(1)).delete(registrationRequest1.getUserUid());
        verify(userService, times(1)).delete(registrationRequest2.getUserUid());
    }

}