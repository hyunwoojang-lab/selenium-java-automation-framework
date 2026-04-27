@SmokeScenario
Feature: User search

  @Smoke @Search
  Scenario: User can see product cards after searching for a keyword
    Given User connect to the base url
    When User click the search box
    And User enter search keyword "무신사 스탠다드" and press enter
    Then User can see product cards displayed in search results
    And User can see "무신사 스탠다드" in the first product card title

  # Negative: 존재하지 않는 키워드로 검색 시 상품 카드가 표시되지 않아야 함
  @Search @Negative
  Scenario: User cannot see product cards when searching for a non-existent keyword
    Given User connect to the base url
    When User click the search box
    And User enter search keyword "zzzzzzzzzzzzzzzzzzzzz" and press enter
    Then User can see "검색 결과가 없습니다." and "다른 검색어를 입력해 보세요." messages in search results
#    Then User can see no product cards displayed in search results

  # Edge: 공백만 입력하고 검색 시 검색이 실행되지 않아야 함
  @Search @Edge
  Scenario: User cannot search with whitespace-only keyword
    Given User connect to the base url
    When User click the search box
    And User enter search keyword "   " and press enter
    Then User can see the "검색어를 입력하세요" message displayed in the browser popup