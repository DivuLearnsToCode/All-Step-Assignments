package com.gdb.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Loads a .properties file into memory once, tried first on the classpath
 * (so packaged/Maven-style builds work), then falling back to a direct
 * filesystem path (so it also runs from a plain javac/java setup).
 *
 * Two access styles are exposed:
 *  - getProperty / getDouble: raw single-key lookups (Activity 14 style).
 *  - getBucketFeatureMap: a generic parser that scans EVERY key in the file,
 *    and for any key shaped "<prefix>.<bucket>" where <bucket> is one of the
 *    recognized tenure buckets, converts the (possibly dotted) prefix into a
 *    camelCase feature name and buckets the value accordingly. This is what
 *    lets "daily.transfer.limit.new" become bucket "new", feature
 *    "dailyTransferLimit" -- with no special-casing per feature.
 */
public class AccountRulesPropertiesLoader {

    private static final Set<String> TENURE_BUCKETS = Set.of("new", "standard", "premium", "privilege");

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

    /** Retrieve a raw text value, or defaultValue if the key is missing. */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /** Retrieve a value parsed as a double, or defaultValue if missing/unparseable. */
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

    /**
     * Generic pass over every key in the file: bucket -> featureKey -> value.
     * Adding a brand new feature (like dailyTransferLimit) needs nothing here
     * beyond new lines in the .properties file, because the prefix-to-feature
     * conversion is fully generic (dot-separated words -> camelCase).
     */
    public Map<String, Map<String, Double>> getBucketFeatureMap() {
        Map<String, Map<String, Double>> result = new LinkedHashMap<>();

        for (String key : properties.stringPropertyNames()) {
            int lastDot = key.lastIndexOf('.');
            if (lastDot < 0) {
                continue;
            }
            String bucket = key.substring(lastDot + 1);
            if (!TENURE_BUCKETS.contains(bucket)) {
                continue;
            }
            String featureKey = toCamelCase(key.substring(0, lastDot));
            double value = getDouble(key, 0.0);

            result.computeIfAbsent(bucket, b -> new LinkedHashMap<>()).put(featureKey, value);
        }

        return result;
    }

    private static String toCamelCase(String dottedPrefix) {
        String[] parts = dottedPrefix.split("\\.");
        StringBuilder sb = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }

    public String getLoadedFrom() {
        return loadedFrom;
    }
}
