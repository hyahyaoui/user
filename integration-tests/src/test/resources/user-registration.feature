Feature: User Registration

  Scenario: Validating a user registration
    Given a user which username is 'user' and email is 'user@company.com'
    When this user register to the application
    Then a registration request is created referencing the user, and user is created with status 'INACTIVE'
    And when the user try to validate his registration with the given registration code in time
    Then  his status should pass to 'ACTIVE' and the registration request should not exist any more


  Scenario: User and it's registration should be removed when the user did not validate his registration at time
    Given a user which username is 'user' and email is 'user@company.com'
    When this user register to the application
    Then a registration request is created referencing the user, and user is created with status 'INACTIVE'
    And when the user exceed the registration deadline and the cleanup cron runs
    Then his account should not exist any more neither the registration request

