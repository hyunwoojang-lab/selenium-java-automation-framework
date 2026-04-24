package managers;

import dataProviders.ConfigFileReader;

public class FileReaderManager {
    private static final FileReaderManager instance = new FileReaderManager();
    private ConfigFileReader configFileReader;

    private FileReaderManager() {}

    public static FileReaderManager getInstance() {
        return instance;
    }

    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? configFileReader = new ConfigFileReader() : configFileReader;
    }
}