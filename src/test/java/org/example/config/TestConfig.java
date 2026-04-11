package org.example.config;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties properties = new Properties();
    private static final String MOCK_KEY = "mock.enabled";

    static {
        try (InputStream is = TestConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is != null) {
                properties.load(is);
                System.out.println("[CONFIG] SUCCESS: config.properties loaded.");
                // Optional: Print the value it found just to be absolutely sure
                System.out.println("[CONFIG] Value of mock.enabled in file: " + properties.getProperty(MOCK_KEY));
            } else {
                // If 'is' is null, Java couldn't find the file in the classpath
                System.out.println("[CONFIG] WARNING: config.properties NOT FOUND. Defaulting to CLI or hardcoded false.");
            }

        } catch (Exception e) {
            System.err.println("[CONFIG] ERROR reading config.properties: " + e.getMessage());
        }
    }

    public static boolean isMockingEnabled() {
        // Check from CLI adding -Dmock.enabled=true next to the mvn clean test
        String cliValue = System.getProperty(MOCK_KEY);
        if (cliValue != null) {
            return Boolean.parseBoolean(cliValue);
        }

        // Check from properties file
        String fileValue = properties.getProperty(MOCK_KEY);
        if (fileValue != null) {
            return Boolean.parseBoolean(fileValue);
        }

        // False and not trigger the infra hooks
        return false;
    }

}
