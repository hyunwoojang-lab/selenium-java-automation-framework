# Selenium · Cucumber · Java E2E Automation Framework

> BDD 기반 E2E 자동화 프레임워크 | Selenium + Cucumber + Java

---

## Overview

**Selenium 4 + Cucumber 7 + Java 11** 조합으로 BDD 방식의 E2E 자동화 프레임워크를 구성했습니다.

PicoContainer 기반 의존성 주입(DI)으로 Step 클래스 간 상태를 공유하고,
Manager 계층을 분리하여 WebDriver · PageObject · CDP 인터셉터를 일관성 있게 관리합니다.
Chrome DevTools Protocol(CDP)을 활용한 API 인터셉트 기능을 내장하고 있습니다.

---

## 프로젝트 구조

```
selenium-cucumber-java-framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── cucumber/
│   │   │   │   └── TestContext.java             # Step 간 공유 컨텍스트 (DI 루트)
│   │   │   ├── dataProviders/
│   │   │   │   └── ConfigFileReader.java         # properties 파일 파싱
│   │   │   ├── enums/
│   │   │   │   ├── DriverType.java               # CHROME / FIREFOX / EDGE
│   │   │   │   └── EnvironmentType.java          # LOCAL / REMOTE
│   │   │   ├── managers/
│   │   │   │   ├── CdpNetworkInterceptor.java    # CDP 기반 API Mock 처리
│   │   │   │   ├── FileReaderManager.java        # ConfigFileReader 싱글톤 관리
│   │   │   │   ├── PageObjectManager.java        # Page 객체 Lazy 초기화
│   │   │   │   └── WebDriverManager.java         # WebDriver 생성 · 종료
│   │   │   └── pageObjects/
│   │   │       └── LoginPage.java                # 로그인 페이지 POM
│   │   └── resources/
│   │       └── log4j2.xml                        # 로그 설정
│   └── test/
│       ├── java/
│       │   ├── stepDefinitions/
│       │   │   ├── Hooks.java                    # Before / After (브라우저 생명주기)
│       │   │   ├── LoginSteps.java               # 로그인 시나리오 Step 구현
│       │   │   └── NetworkInterceptSteps.java    # CDP 인터셉트 Step 구현
│       │   └── testRunner/
│       │       └── testRunner.java               # Cucumber 실행 설정
│       └── resources/
│           └── Features/
│               ├── login.feature                 # 로그인 시나리오
│               └── networkIntercept.feature      # CDP 인터셉트 시나리오
├── configs/
│   └── Configuration.properties                  # 실행 환경 설정
└── pom.xml
```

---

## 기술 스택

| 분류 | 기술 | 버전 |
|---|---|---|
| 언어 | Java | 11 |
| 자동화 | Selenium | 4.41 |
| BDD | Cucumber | 7.34 |
| 테스트 러너 | JUnit + Cucumber-JUnit | - |
| DI 컨테이너 | PicoContainer | 7.34 |
| 빌드 | Maven | 3.6+ |
| 로깅 | Log4j2 | 2.25 |
| CDP | Chrome DevTools Protocol | Selenium 내장 |

---

## 주요 설계 결정

### 1. TestContext — PicoContainer 기반 Step 간 상태 공유

Cucumber에서 여러 Step 클래스가 동일한 WebDriver · PageObject · Interceptor 인스턴스를
공유하려면 상태 전달 수단이 필요합니다.

`TestContext`를 DI 루트로 두고 PicoContainer가 각 Step 클래스의 생성자에
자동으로 주입하도록 구성했습니다.
덕분에 Step 클래스는 `TestContext`만 받으면 모든 의존성에 접근할 수 있습니다.

```java
// TestContext — 모든 Manager를 한 곳에서 초기화
public class TestContext {
    private WebDriverManager webDriverManager;
    private PageObjectManager pageObjectManager;
    private CdpNetworkInterceptor cdpNetworkInterceptor;

    public TestContext() {
        webDriverManager      = new WebDriverManager();
        pageObjectManager     = new PageObjectManager(webDriverManager.getDriver());
        cdpNetworkInterceptor = new CdpNetworkInterceptor(webDriverManager.getDriver());
    }
}

// Step 클래스 — 생성자에서 TestContext 주입받아 사용
public class LoginSteps {
    private LoginPage loginPage;

    public LoginSteps(TestContext context) {
        loginPage = context.getPageObjectManager().getLoginPage();
    }
}

// Hooks — 동일한 TestContext로 WebDriver 종료
public class Hooks {
    TestContext testContext;

    public Hooks(TestContext context) {
        testContext = context;
    }

    @Before
    public void beforeSteps() {
        if (FileReaderManager.getInstance().getConfigReader().isWindowMaximize()) {
            testContext.getWebDriverManager().getDriver().manage().window().maximize();
        }
    }

    @After
    public void afterSteps() {
        testContext.getWebDriverManager().closeDriver();
    }
}
```

---

### 2. Manager 계층 분리

각 역할을 독립적인 Manager 클래스로 분리해 단일 책임 원칙을 지켰습니다.

**FileReaderManager — 싱글톤으로 설정 파일 중복 로드 방지**

```java
public class FileReaderManager {
    private static final FileReaderManager instance = new FileReaderManager();
    private ConfigFileReader configFileReader;

    private FileReaderManager() {}

    public static FileReaderManager getInstance() {
        return instance;
    }

    // Lazy initialization — 최초 호출 시에만 ConfigFileReader 생성
    public ConfigFileReader getConfigReader() {
        return (configFileReader == null)
            ? configFileReader = new ConfigFileReader()
            : configFileReader;
    }
}
```

**WebDriverManager — 브라우저 타입별 WebDriver 생성**

```java
public WebDriver createDriver() {
    DriverType driverType = FileReaderManager.getInstance()
                                            .getConfigReader()
                                            .getBrowser();
    switch (driverType) {
        case FIREFOX: return new FirefoxDriver();
        case EDGE:    return new EdgeDriver();
        default:      return new ChromeDriver();   // CHROME
    }
}
```

**PageObjectManager — Page 객체 Lazy 초기화**

```java
public LoginPage getLoginPage() {
    // 최초 접근 시에만 생성, 이후에는 캐싱된 인스턴스 반환
    return (loginPage == null)
        ? loginPage = new LoginPage(driver)
        : loginPage;
}
```

**ConfigFileReader — 타입 안전한 설정 로드**

```java
public DriverType getBrowser() {
    String browser = properties.getProperty("browser");
    switch (browser.toLowerCase()) {
        case "chrome":  return DriverType.CHROME;
        case "firefox": return DriverType.FIREFOX;
        case "edge":    return DriverType.EDGE;
        default: throw new RuntimeException("지원하지 않는 browser 값: " + browser);
    }
}
```

---

### 3. @FindBy 대신 By 변수 직접 선언

`@FindBy` 어노테이션은 PageFactory를 통해 필드를 초기화하는 방식으로,
**인덱스나 파라미터를 조합해 동적으로 로케이터를 생성해야 하는 경우에는 사용할 수 없습니다.**

예를 들어 동일한 클래스명의 에러 메시지가 여러 개 표시될 때,
각 메시지를 XPath 인덱스로 순서대로 가져와야 하는 케이스가 있습니다.
이런 상황을 일관성 있게 처리하기 위해 **By 변수로 선언하는 방식으로 통일**했습니다.

```java
// By 변수로 로케이터 선언
private By wrongPasswordAlertText = By.className("login-v2-input__validation");

public String getAlertForWrongPassword() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(wrongPasswordAlertText));

    int alertCount = driver.findElements(wrongPasswordAlertText).size();
    logger.info("Alert Message Count : {}", alertCount);

    if (alertCount == 1) {
        return driver.findElement(wrongPasswordAlertText).getText();

    } else if (alertCount > 1) {
        // 인덱스 기반 XPath 동적 조합 — @FindBy로는 처리 불가
        String baseXpath = "//*[contains(@class,'login-v2-input__validation')]";
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= alertCount; i++) {
            String text = driver.findElement(
                By.xpath("(" + baseXpath + ")[" + i + "]")
            ).getText();
            result.append(text);
            if (i < alertCount) result.append("\n");
        }
        return result.toString();
    }
    return "";
}
```

---

### 4. CDP 기반 API 인터셉트 — CdpNetworkInterceptor

Chrome DevTools Protocol(CDP)의 `Fetch.enable` 을 활용해
실제 서버 요청 없이 즉시 Mock 응답을 반환합니다.

URL 패턴별 Mock 응답을 `LinkedHashMap`으로 관리해 등록 순서를 보장하고,
glob 패턴(`*urlPattern*`)으로 부분 URL 매칭을 지원합니다.

```java
// Mock 응답 등록
public void addMockResponse(String urlPattern, int statusCode,
                             String contentType, String body) {
    mockResponseMap.put(urlPattern,
        new MockResponse(statusCode, contentType, body));
}

// 인터셉터 활성화
public void enable() {
    // 등록된 패턴 기준으로 REQUEST 단계에서 인터셉트
    List<RequestPattern> patterns = mockResponseMap.keySet().stream()
        .map(url -> new RequestPattern(
            Optional.of("*" + url + "*"),   // glob 패턴으로 부분 매칭
            Optional.empty(),
            Optional.of(RequestStage.REQUEST)
        ))
        .collect(Collectors.toList());

    devTools.send(Fetch.enable(Optional.of(patterns), Optional.of(false)));

    devTools.addListener(Fetch.requestPaused(), requestPaused -> {
        String requestUrl = requestPaused.getRequest().getUrl();

        Optional<Map.Entry<String, MockResponse>> matched =
            mockResponseMap.entrySet().stream()
                .filter(entry -> requestUrl.contains(entry.getKey()))
                .findFirst();

        if (matched.isPresent()) {
            MockResponse mock = matched.get().getValue();
            // 실제 서버 요청 없이 즉시 Mock 응답 반환
            devTools.send(Fetch.fulfillRequest(
                requestPaused.getRequestId(),
                mock.statusCode,
                Optional.of(List.of(new HeaderEntry("Content-Type", mock.contentType))),
                Optional.empty(),
                Optional.of(Base64.getEncoder().encodeToString(mock.body.getBytes())),
                Optional.empty()
            ));
        } else {
            // 패턴 미매칭 요청은 그대로 통과
            devTools.send(Fetch.continueRequest(
                requestPaused.getRequestId(),
                Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty()
            ));
        }
    });
}
```

---

### 5. BDD 시나리오 — 태그 전략

**login.feature — 로그인 기능 (Positive / Negative / Edge)**

```gherkin
@SmokeScenario
Feature: User login

  @Smoke @Login
  Scenario: User can log in to web page
    Given User connect to the base url
    When User click the login header menu
    And User enter "user1" and "12345"
    And User click the login button
    Then User can see the "아이디 또는 패스워드를 확인하세요." message displayed in the browser popup

  # Negative: 비밀번호 없이 아이디만 입력하고 로그인 시도
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
```

**networkIntercept.feature — CDP 인터셉트 전후 응답 시간 비교**

```gherkin
@NetworkIntercept
Feature: CDP based API intercept

  # 인터셉트 없이 느린 API 호출 — 3초 이상 소요
  Scenario: Slow API call without interceptor takes more than 3 seconds
    Given Browser navigates to "https://httpbin.org/delay/3"
    Then Response time is more than 2000 milliseconds

  # 인터셉트 적용 후 동일한 API 가 즉시 응답
  Scenario: Same slow API responds immediately after interceptor is applied
    Given CDP interceptor is set up for "httpbin.org/delay" with status 200 and body "{\"mocked\":true,\"message\":\"Intercepted response\"}"
    When Browser navigates to "https://httpbin.org/delay/3"
    Then Response time is less than 1000 milliseconds
    And Page source contains "Intercepted response"
    And CDP interceptor is disabled
```

**태그 분류 기준**

| 태그 | 용도 |
|---|---|
| `@Smoke` | 핵심 기능 빠른 검증 — 배포 전 필수 실행 |
| `@Login` | 로그인 기능 관련 전체 |
| `@Negative` | 실패 케이스 — 잘못된 입력값 검증 |
| `@Edge` | 경계값 · 예외 케이스 |
| `@NetworkIntercept` | CDP 인터셉트 관련 시나리오 |