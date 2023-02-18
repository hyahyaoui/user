package com.knsoft.user;

import com.knsoft.user.model.User;
import com.knsoft.user.model.UserRegistrationRequest;
import com.knsoft.user.repositories.InMemoryUserRegistrationRequestRepository;
import com.knsoft.user.repositories.InMemoryUserRepository;
import com.knsoft.user.repositories.UserRepository;
import com.knsoft.user.services.UserRegistrationService;
import com.knsoft.user.services.UserService;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class UserRegistrationStepDefinitions {
    private UserService userService;
    private UserRegistrationService userRegistrationService;
    private UserRepository userRepository = new InMemoryUserRepository();
    private InMemoryUserRegistrationRequestRepository userRegistrationRequestRepository =
            new InMemoryUserRegistrationRequestRepository();

    private User user;

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
        userRegistrationService = new UserRegistrationService(userRegistrationRequestRepository,
                userService, 5);
    }

    @Given("a user which username is {string} and email is {string}")
    public void userWithGivenUsernameAndEmail(String username, String email) {
        user = new User();
        user.setUserName(username);
        user.setEmail(email);

    }

    @When("this user register to the application")
    public void thisUserRegisterToTheApplication() {
        user = userRegistrationService.register(user);
    }

    @Then("a registration request is created referencing the user, and user is created with status {string}")
    public void aRegistrationRequestIsCreatedForTheUserAndUserIsCreatedWithGivenStatus(String status) {
        assertEquals(User.Status.valueOf(status), user.getStatus());
        final UserRegistrationRequest registration = userRegistrationRequestRepository
                .findRegistrationRequestByUserUid(user.getUid()).get();
        assertEquals(registration.getUserUid(), user.getUid());

    }

    @And("when the user try to validate his registration with the given registration code in time")
    public void whenTheUserTryToValidateHisRegistrationWithTheGivenRegistrationCodeInTime(){
        final UserRegistrationRequest registration =
                userRegistrationRequestRepository.findRegistrationRequestByUserUid(user.getUid()).get();

        userRegistrationService.validateRegistration(registration.getUid());

    }

    @Then("his status should pass to {string} and the registration request should not exist any more")
    public void hisStatusShouldPassToACTIVEAndTheRegistrationRequestShouldNotExistAnyMore(String status) {

        user = userService.findUserByUid(user.getUid()).get();

        assertEquals(user.getStatus(), User.Status.valueOf(status));
        assertEquals(userRegistrationRequestRepository.findRegistrationRequestByUserUid(user.getUid()), Optional.empty());
    }

    @And("when the user exceed the registration deadline and try to validate his registration")
    public void whenTheUserExceedTheRegistrationDeadlineAndTryToValidateHisRegistration() {


    }

    @Then("his account should not exist any more neither the registration request")
    public void hisAccountShouldNotExistAnyMoreNeitherTheRegistrationRequest() {
    }
}
