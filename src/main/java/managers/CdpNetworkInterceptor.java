package managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v145.fetch.Fetch;
import org.openqa.selenium.devtools.v145.fetch.model.HeaderEntry;
import org.openqa.selenium.devtools.v145.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v145.fetch.model.RequestStage;

import java.util.*;
import java.util.stream.Collectors;

public class CdpNetworkInterceptor {
    private static final Logger logger = LogManager.getLogger(CdpNetworkInterceptor.class);

    private final DevTools devTools;
    // urlPattern → MockResponse 순서 보장을 위해 LinkedHashMap 사용
    private final Map<String, MockResponse> mockResponseMap = new LinkedHashMap<>();

    public static class MockResponse {
        public final int statusCode;
        public final String contentType;
        public final String body;

        public MockResponse(int statusCode, String contentType, String body) {
            this.statusCode = statusCode;
            this.contentType = contentType;
            this.body = body;
        }
    }

    public CdpNetworkInterceptor(WebDriver driver) {
        devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        logger.info("CDP DevTools 세션 생성 완료");
    }

    public void addMockResponse(String urlPattern, int statusCode, String contentType, String body) {
        mockResponseMap.put(urlPattern, new MockResponse(statusCode, contentType, body));
        logger.info("Mock 응답 등록 - URL 패턴: [{}], 상태코드: {}", urlPattern, statusCode);
    }

    public void enable() {
        // 등록된 urlPattern 기준으로 REQUEST 단계에서 인터셉트
        // CDP RequestPattern 은 glob 패턴 사용 → "*패턴*" 형태로 감싸야 부분 URL 매칭됨
        List<RequestPattern> patterns = mockResponseMap.keySet().stream()
                .map(url -> new RequestPattern(
                        Optional.of("*" + url + "*"),
                        Optional.empty(),
                        Optional.of(RequestStage.REQUEST)
                ))
                .collect(Collectors.toList());

        devTools.send(Fetch.enable(Optional.of(patterns), Optional.of(false)));

        devTools.addListener(Fetch.requestPaused(), requestPaused -> {
            String requestUrl = requestPaused.getRequest().getUrl();

            Optional<Map.Entry<String, MockResponse>> matched = mockResponseMap.entrySet().stream()
                    .filter(entry -> requestUrl.contains(entry.getKey()))
                    .findFirst();

            if (matched.isPresent()) {
                MockResponse mock = matched.get().getValue();
                logger.info("인터셉트 성공: {} → 실제 요청 대신 Mock 응답 반환", requestUrl);

                // 실제 서버 요청 없이 즉시 Mock 응답으로 대체
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
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                ));
            }
        });

        logger.info("CDP Network Interceptor 활성화 - 등록된 패턴 수: {}", mockResponseMap.size());
    }

    public void disable() {
        devTools.send(Fetch.disable());
        mockResponseMap.clear();
        logger.info("CDP Network Interceptor 비활성화");
    }
}