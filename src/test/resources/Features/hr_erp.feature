@SmokeScenario
Feature: User login in the ERP

  @Smoke @Login
  Scenario: Create the new employee
    Given User connect to "https://opensource-demo.orangehrmlive.com/"
    When User enter "Admin" and "admin123" in the ERP
    And User click the login button in the ERP
    And User click the "PIM" side menu
    And User click the Add button
    And User select the "First Name" input field and enter "user135"
    And User select the "Last Name" input field and enter "00"
    And User select the "Employee Id" field and enter "1"
    And User click create user toggle
    And User select the "Username" field and enter "user135"
    And User select the "Password" field and enter "pw13579"
    And User select the "Confirm Password" field and enter "pw13579"
    And User click the "Save" button in the pim page
    Then User can see "Successfully Saved" message

  Scenario: User can login by new employee to the ERP
    Given User connect to "https://opensource-demo.orangehrmlive.com/"
    When User enter "user135" and "pw13579" in the ERP
    And User click the login button in the ERP
    Then User can see the username and dashboard page

  Scenario: Delete the employee
    Given User connect to "https://opensource-demo.orangehrmlive.com/"
    When User enter "Admin" and "admin123" in the ERP
    And User click the login button in the ERP
    And User click the "PIM" side menu
    And User select the "Employee Name" field and enter "user135"
    And User click the "Search" button in the pim page
    And User click the trash button of the "user135" user
    And User click the "Yes, Delete" button in the pim page
    Then User can see "Successfully Deleted" message


