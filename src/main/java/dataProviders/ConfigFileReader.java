package dataProviders;

import enums.DriverType;
import enums.EnvironmentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
    private static final Logger logger = LogManager.getLogger(ConfigFileReader.class);
    private static final String PROPERTY_FILE_PATH = "configs/Configuration.properties";

    private final Properties properties;

    public ConfigFileReader() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PROPERTY_FILE_PATH))) {
            properties = new Properties();
            properties.load(reader);
            logger.info("Configuration.properties 로드 완료");
        } catch (IOException e) {
            throw new RuntimeException("Configuration.properties 를 찾을 수 없습니다: " + PROPERTY_FILE_PATH, e);
        }
    }

    public String getUrl() {
        String url = properties.getProperty("url");
        if (url == null) throw new RuntimeException("url 이 Configuration.properties 에 설정되지 않았습니다.");
        return url;
    }

    public DriverType getBrowser() {
        String browser = properties.getProperty("browser");
        if (browser == null) throw new RuntimeException("browser 가 Configuration.properties 에 설정되지 않았습니다.");
        switch (browser.toLowerCase()) {
            case "chrome":  return DriverType.CHROME;
            case "firefox": return DriverType.FIREFOX;
            case "edge":    return DriverType.EDGE;
            default: throw new RuntimeException("지원하지 않는 browser 값: " + browser);
        }
    }

    public boolean isWindowMaximize() {
        String value = properties.getProperty("windowMaximize");
        return value == null || Boolean.parseBoolean(value);
    }

    public EnvironmentType getEnvironment() {
        String env = properties.getProperty("environment");
        if (env == null) return EnvironmentType.LOCAL;
        switch (env.toLowerCase()) {
            case "local":  return EnvironmentType.LOCAL;
            case "remote": return EnvironmentType.REMOTE;
            default: throw new RuntimeException("지원하지 않는 environment 값: " + env);
        }
    }
}