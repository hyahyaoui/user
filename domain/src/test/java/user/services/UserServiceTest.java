package user.services;

import com.knsoft.commons.data.Page;
import com.knsoft.commons.data.exceptions.EntityAlreadyExistsException;
import com.knsoft.commons.data.exceptions.EntityNotFoundException;
import com.knsoft.user.model.User;
import com.knsoft.user.repositories.UserRepository;
import com.knsoft.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }


    @Test
    void should_find_all_users() {
        // Arrange
        int start = 0;
        int end = 10;
        int resultsPerPage = 5;
        List<User> userList = Collections.emptyList();
        Page<User> page = new Page.Builder<User>()
                .withCurrentPage(0)
                .withNumberOfPages(1)
                .withResultsPerPage(resultsPerPage)
                .withResults(userList)
                .build();
        when(userRepository.findAllUsers(start, end, resultsPerPage)).thenReturn(page);

        // Act
        Page<User> result = userService.findAll(start, end, resultsPerPage);

        // Assert
        assertEquals(page, result);
        verify(userRepository).findAllUsers(start, end, resultsPerPage);
    }

    @Test
    void should_find_user_by_uid() {
        // Arrange
        User user = new User();
        String uid = user.getUid();

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);

        // Act
        Optional<User> result = userService.findUserByUid(uid);

        // Assert
        assertEquals(optionalUser, result);
        verify(userRepository).findUserById(uid);
    }

    @Test
    void should_throw_an_IllegalArgumentException_when_trying_to_find_user_with_uid_equal_null() {
        // Arrange
        String uid = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.findUserByUid(uid));
        verify(userRepository, never()).findUserById(any());
    }

    @Test
    void should_find_user_by_username() {
        // Arrange
        String userName = "username";
        User user = new User();
        user.setUserName(userName);

        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findUserByUserName(userName)).thenReturn(optionalUser);

        // Act
        Optional<User> result = userService.findByUserName(userName);

        // Assert
        assertEquals(optionalUser, result);
        verify(userRepository).findUserByUserName(userName);
    }

    @Test
    void should_throw_an_IllegalArgumentException_when_trying_to_find_user_with_username_equal_null() {
        // Arrange
        String username = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.findByUserName(username));
        verify(userRepository, never()).findUserById(any());
    }

    @Test
    void should_create_user() {
        // Arrange
        User user = new User();
        when(userRepository.create(user)).thenReturn(user);

        // Act
        User result = userService.create(user);

        // Assert
        assertEquals(user, result);
        verify(userRepository).create(user);
    }

    @Test
    void should_throw_an_IllegalArgumentException_when_trying_to_create_null_user() {
        // Arrange
        User user = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.create(user));
        verify(userRepository, never()).create(any());
    }

    @Test
    void should_throw_an_EntityAlreadyExistsException_when_trying_to_create_user_with_existing_email() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EntityAlreadyExistsException.class, () -> userService.create(user));
        verify(userRepository, never()).create(any());
    }

    @Test
    void should_throw_an_EntityAlreadyExistsException_when_trying_to_create_user_with_existing_username() {
        // Arrange
        User user = new User();
        user.setUserName("username");
        when(userRepository.findUserByUserName("username")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EntityAlreadyExistsException.class, () -> userService.create(user));
        verify(userRepository, never()).create(any());
    }

    @Test
    void should_update_user() {
        // Arrange

        User user = new User();
        String uid = user.getUid();
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);
        when(userRepository.update(uid, user)).thenReturn(user);

        // Act
        User result = userService.update(uid, user);

        // Assert
        assertEquals(user, result);
        verify(userRepository).findUserById(uid);
        verify(userRepository).update(uid, user);
    }

    @Test
    void should_throw_an_IllegalArgumentException_when_trying_to_update_null_user() {
        // Arrange
        String uid = "123";
        User user = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.update(uid, user));
        verify(userRepository, never()).update(any(), any());
    }

    @Test
    void should_throw_an_EntityNotFoundException_when_trying_to_update_a_non_existing_user() {
        // Arrange
        User user = new User();
        String uid = user.getUid();
        Optional<User> optionalUser = Optional.empty();
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.update(uid, user));
        verify(userRepository).findUserById(uid);
        verify(userRepository, never()).update(any(), any());
    }

    @Test
    void should_throw_an_EntityAlreadyExistsException_when_trying_to_update_user_with_existing_email() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EntityAlreadyExistsException.class, () -> userService.update("uid", user));
        verify(userRepository, never()).create(any());
    }

    @Test
    void should_throw_an_EntityAlreadyExistsException_when_trying_to_update_user_with_existing_username() {
        // Arrange
        User user = new User();
        user.setUserName("username");
        when(userRepository.findUserByUserName("username")).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EntityAlreadyExistsException.class, () -> userService.update("uid", user));
        verify(userRepository, never()).create(any());
    }

    @Test
    void should_activate_user() {
        // Arrange
        User user = new User();
        String uid = user.getUid();
        user.setStatus(User.Status.INACTIVE);
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);
        when(userRepository.update(uid, user)).thenReturn(user);

        // Act
        User result = userService.activateUser(uid);

        // Assert
        assertEquals(User.Status.ACTIVE, result.getStatus());
        verify(userRepository).findUserById(uid);
        verify(userRepository).update(uid, user);
    }

    @Test
    void should_throw_an_IllegalArgumentException_when_trying_to_activate_null_user() {
        // Arrange
        String uid = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.activateUser(uid));
        verify(userRepository, never()).findUserById(any());
        verify(userRepository, never()).update(any(), any());
    }

    @Test
    void should_throw_an_EntityNotFoundException_when_trying_to_activate_a_non_existing_user() {
        // Arrange
        String uid = "123";
        Optional<User> optionalUser = Optional.empty();
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.activateUser(uid));
        verify(userRepository).findUserById(uid);
        verify(userRepository, never()).update(any(), any());
    }

    @Test
    void should_delete_user() {
        // Arrange
        User user = new User();
        String uid = user.getUid();
        Optional<User> optionalUser = Optional.of(user);
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);

        // Act
        userService.delete(uid);

        // Assert
        verify(userRepository).findUserById(uid);
        verify(userRepository).delete(uid);
    }

    @Test
    void should_throw_an_IllegalArgumentException_when_trying_to_delete_user_with_uid_equal_null() {
        // Arrange
        String uid = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.delete(uid));
        verify(userRepository, never()).findUserById(any());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void should_throw_an_EntityNotFoundException_when_trying_to_delete_a_non_existing_user() {
        // Arrange
        String uid = "123";
        Optional<User> optionalUser = Optional.empty();
        when(userRepository.findUserById(uid)).thenReturn(optionalUser);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.delete(uid));
        verify(userRepository).findUserById(uid);
        verify(userRepository, never()).delete(any());
    }
}