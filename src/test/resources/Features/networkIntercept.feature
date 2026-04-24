@NetworkIntercept
Feature: CDP based API intercept

  # httpbin.org/delay/3 은 응답까지 3초가 걸리는 테스트용 엔드포인트
  # 인터셉트 없이 느린 API 를 호출하면 3초 이상 걸린다
  Scenario: Slow API call without interceptor takes more than 3 seconds
    Given Browser navigates to "https://httpbin.org/delay/3"
    Then Response time is more than 2000 milliseconds


  # 인터셉트 적용 후 동일한 느린 API 가 즉시 응답된다
  Scenario: Same slow API responds immediately after interceptor is applied
    Given CDP interceptor is set up for "httpbin.org/delay" with status 200 and body "{\"mocked\":true,\"message\":\"Intercepted response\"}"
    When Browser navigates to "https://httpbin.org/delay/3"
    Then Response time is less than 1000 milliseconds
    And Page source contains "Intercepted response"
    And CDP interceptor is disabled