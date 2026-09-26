import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads a .properties file into memory once, tried first on the classpath
 * (so packaged/Maven-style builds work), then falling back to a direct
 * filesystem path (so it also runs from a plain javac/java setup). Exposes
 * typed getters so callers never touch java.util.Properties directly.
 */
public class AccountRulesPropertiesLoader {

    private final Properties properties = new Properties();
    private final String loadedFrom;

    public AccountRulesPropertiesLoader(String classpathResource, String fileSystemPath) {
        String source = tryLoadFromClasspath(classpathResource);

        if (source == null) {
            source = tryLoadFromFileSystem(fileSystemPath);
        }

        if (source == null) {
            throw new IllegalStateException(
                    "Could not load properties from classpath [" + classpathResource +
                            "] or file system [" + fileSystemPath + "]");
        }

        this.loadedFrom = source;
        System.out.println("[Config] Loaded rules from " + loadedFrom);
    }

    private String tryLoadFromClasspath(String classpathResource) {
        try (InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(classpathResource)) {
            if (in == null) {
                return null;
            }
            properties.load(in);
            return classpathResource;
        } catch (IOException e) {
            return null;
        }
    }

    private String tryLoadFromFileSystem(String fileSystemPath) {
        try (InputStream in = new FileInputStream(fileSystemPath)) {
            properties.load(in);
            return fileSystemPath;
        } catch (IOException e) {
            return null;
        }
    }

    /** Step 2: retrieve a raw text value, or defaultValue if the key is missing. */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /** Step 2: retrieve a value parsed as a double, or defaultValue if missing/unparseable. */
    public double getDouble(String key, double defaultValue) {
        String raw = properties.getProperty(key);
        if (raw == null || raw.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
