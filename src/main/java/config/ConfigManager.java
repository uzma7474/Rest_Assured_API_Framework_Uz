package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigManager
 *
 * Centralized configuration manager for the REST Assured framework.
 *
 * Configuration loading order:
 *
 * 1. config.properties 2. <environment>.properties
 *
 * Environment is selected using:
 *
 * -Denv=qa -Denv=prod
 *
 * Default environment:
 *
 * qa
 *
 * Example:
 *
 * mvn test -Denv=qa mvn test -Denv=prod
 */
public final class ConfigManager {

	private static final Properties PROPERTIES = new Properties();

	private static final String DEFAULT_ENVIRONMENT = "qa";

	private static final String COMMON_CONFIG_FILE = "config/config.properties";

	private ConfigManager() {
		// Prevent object creation
	}

	/*
	 * Load configuration when the class is initialized.
	 */
	static {
		loadProperties();
	}

	/**
	 * Loads common configuration followed by environment-specific configuration.
	 *
	 * Example:
	 *
	 * config.properties + qa.properties
	 *
	 * Environment-specific properties override common properties.
	 */
	private static void loadProperties() {

		String environment = System.getProperty("env", DEFAULT_ENVIRONMENT).trim().toLowerCase();

		/*
		 * Validate environment name.
		 */
		if (environment.isEmpty()) {

			environment = DEFAULT_ENVIRONMENT;
		}

		/*
		 * Load common configuration.
		 */
		loadFile(COMMON_CONFIG_FILE);

		/*
		 * Load environment-specific configuration.
		 *
		 * Example:
		 *
		 * config/qa.properties config/prod.properties
		 */
		String environmentFile = "config/" + environment + ".properties";

		loadFile(environmentFile);

		System.out.println("==========================================");

		System.out.println("Configuration Loaded");

		System.out.println("Environment: " + environment);

		System.out.println("Base URL: " + PROPERTIES.getProperty("base.url"));

		System.out.println("==========================================");
	}

	/**
	 * Loads a properties file from classpath.
	 *
	 * @param fileName properties file path
	 */
	private static void loadFile(String fileName) {

		try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(fileName)) {

			if (inputStream == null) {

				throw new RuntimeException("Configuration file not found: " + fileName);
			}

			PROPERTIES.load(inputStream);

		} catch (IOException e) {

			throw new RuntimeException("Unable to load configuration file: " + fileName, e);
		}
	}

	/**
	 * Returns a mandatory property.
	 *
	 * Throws an exception if the property is missing or empty.
	 *
	 * @param key property key
	 * @return property value
	 */
	public static String get(String key) {

		if (key == null || key.trim().isEmpty()) {

			throw new IllegalArgumentException("Property key cannot be null or empty.");
		}

		String value = PROPERTIES.getProperty(key);

		if (value == null || value.trim().isEmpty()) {

			throw new RuntimeException("Property not found or empty: " + key);
		}

		return resolveEnvironmentVariable(value.trim());
	}

	/**
	 * Returns a property value.
	 *
	 * If the property does not exist, the supplied default value is returned.
	 *
	 * @param key          property key
	 * @param defaultValue default value
	 * @return property value
	 */
	public static String get(String key, String defaultValue) {

		if (key == null || key.trim().isEmpty()) {

			throw new IllegalArgumentException("Property key cannot be null or empty.");
		}

		String value = PROPERTIES.getProperty(key, defaultValue);

		if (value == null) {

			return null;
		}

		return resolveEnvironmentVariable(value.trim());
	}

	/**
	 * Alias used by RequestSpecificationFactory and other framework classes.
	 *
	 * @param key property key
	 * @return property value
	 */
	public static String getProperty(String key) {

		return get(key);
	}

	/**
	 * Returns property value or default value.
	 *
	 * @param key          property key
	 * @param defaultValue default value
	 * @return property value
	 */
	public static String getProperty(String key, String defaultValue) {

		return get(key, defaultValue);
	}

	/**
	 * Returns API base URL.
	 *
	 * Example:
	 *
	 * https://api.eventhub.rahulshettyacademy.com
	 *
	 * @return API base URL
	 */
	public static String getBaseUrl() {

		return get("base.url");
	}

	/**
	 * Returns API username.
	 *
	 * @return API username
	 */
	public static String getUsername() {

		return get("username", "");
	}

	/**
	 * Returns API password.
	 *
	 * @return API password
	 */
	public static String getPassword() {

		return get("password", "");
	}

	/**
	 * Returns request timeout in milliseconds.
	 *
	 * @return request timeout
	 */
	public static int getRequestTimeout() {

		return getIntProperty("request.timeout", 30000);
	}

	/**
	 * Returns connection timeout in milliseconds.
	 *
	 * @return connection timeout
	 */
	public static int getConnectionTimeout() {

		return getIntProperty("connection.timeout", 30000);
	}

	/**
	 * Returns socket timeout in milliseconds.
	 *
	 * @return socket timeout
	 */
	public static int getSocketTimeout() {

		return getIntProperty("socket.timeout", 30000);
	}

	/**
	 * Returns integer property.
	 *
	 * @param key          property key
	 * @param defaultValue default integer value
	 * @return integer property
	 */
	public static int getIntProperty(String key, int defaultValue) {

		String value = get(key, String.valueOf(defaultValue));

		try {

			return Integer.parseInt(value);

		} catch (NumberFormatException e) {

			throw new RuntimeException("Invalid integer value for property '" + key + "': " + value, e);
		}
	}

	/**
	 * Returns boolean property.
	 *
	 * @param key          property key
	 * @param defaultValue default boolean
	 * @return boolean property
	 */
	public static boolean getBooleanProperty(String key, boolean defaultValue) {

		String value = get(key, String.valueOf(defaultValue));

		return Boolean.parseBoolean(value);
	}

	/**
	 * Returns currently selected environment.
	 *
	 * @return environment name
	 */
	public static String getEnvironment() {

		return System.getProperty("env", DEFAULT_ENVIRONMENT).trim().toLowerCase();
	}

	/**
	 * Resolves environment variables.
	 *
	 * Supports:
	 *
	 * ${API_USERNAME} ${API_PASSWORD}
	 *
	 * Example:
	 *
	 * username=${API_USERNAME}
	 */
	private static String resolveEnvironmentVariable(String value) {

		if (value == null) {
			return null;
		}

		if (value.startsWith("${") && value.endsWith("}")) {

			String variableName = value.substring(2, value.length() - 1);

			String environmentValue = System.getenv(variableName);

			if (environmentValue != null && !environmentValue.isBlank()) {

				return environmentValue;
			}

			/*
			 * Also check Java system properties.
			 *
			 * Example:
			 *
			 * -DAPI_USERNAME=testuser
			 */
			String systemProperty = System.getProperty(variableName);

			if (systemProperty != null && !systemProperty.isBlank()) {

				return systemProperty;
			}

			throw new RuntimeException("Environment variable or system property " + "not found: " + variableName);
		}

		return value;
	}
}
