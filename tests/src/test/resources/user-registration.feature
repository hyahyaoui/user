Feature: User Registration

  Scenario: Validating a user registration
    Given a user which username is 'user' and email is 'user@company.com'
    When this user register to the application
    Then a registration request is created referencing the user, and user is created with status 'INACTIVE'
    And when the user try to validate his registration with the given registration code in time
    Then  his status should pass to 'ACTIVE' and the registration request should not exist any more


  Scenario: Failing to validate a user registration
    Given a user which username is 'user' and email is 'user@company.com'
    When this user register to the application
    Then a registration request is created referencing the user, and user is created with status 'INACTIVE'
    And when the user exceed the registration deadline and try to validate his registration
    Then his account should not exist any more neither the registration request

