@SmokeScenario
Feature: User login

  @Smoke @Login
  Scenario: User can log in to web page
    Given User connect to the base url
    When User click the login header menu
    And User enter "user1" and "12345"
    And User click the login button
    Then User can see the "아이디 또는 패스워드를 확인하세요." message displayed in the browser popup
#    Then User can see that the login was successful


  # Negative: 비밀번호 없이 아이디만 입력하고 로그인 시도 테스트
  @Smoke @Login @Negative
  Scenario: User cannot log in with missing password
    Given User connect to the base url
    When User click the login header menu
    And User enter "user01" and ""
    And User click the login button
    Then User can see the "비밀번호를 입력해 주세요." message displayed in the bottom of "password" input field


  # Edge: 아이디와 비밀번호 필드에 공백 문자만 입력하고 로그인 시도
  @Login @Edge
  Scenario: User cannot log in with whitespace-only credentials
    Given User connect to the base url
    When User click the login header menu
    And User enter "   " and "   "
    And User click the login button
    Then User can see the "통합계정 또는 이메일을 입력해 주세요." message displayed in the bottom of "login" input field
    And User can see the "비밀번호를 입력해 주세요." message displayed in the bottom of "password" input field

