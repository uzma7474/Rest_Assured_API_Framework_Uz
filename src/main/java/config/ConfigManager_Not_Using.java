package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager_Not_Using {

	private static final Properties PROPERTIES = new Properties();

	private ConfigManager_Not_Using() {
		// Prevent object creation
	}

	static {
		loadProperties();
	}

	/**
	 * Loads configuration based on the selected environment.
	 *
	 * Environment can be provided using: -Denv=qa -Denv=stage -Denv=prod
	 *
	 * Default environment: qa
	 */
	private static void loadProperties() {

		String environment = System.getProperty("env", "qa");

		String fileName = "config/" + environment + ".properties";

		try (InputStream inputStream = ConfigManager_Not_Using.class.getClassLoader().getResourceAsStream(fileName)) {

			if (inputStream == null) {
				throw new RuntimeException("Configuration file not found: " + fileName);
			}

			PROPERTIES.load(inputStream);

		} catch (IOException e) {

			throw new RuntimeException("Unable to load configuration file: " + fileName, e);
		}
	}

	/**
	 * Returns a property value.
	 */
	public static String get(String key) {

		String value = PROPERTIES.getProperty(key);

		if (value == null || value.trim().isEmpty()) {

			throw new RuntimeException("Property not found or empty: " + key);
		}

		return value.trim();
	}

	/**
	 * Returns a property value or default value.
	 */
	public static String get(String key, String defaultValue) {

		return PROPERTIES.getProperty(key, defaultValue).trim();
	}

	/**
	 * Returns the configured base URL.
	 */
	public static String getBaseUrl() {

		return get("base.url");
	}

	/**
	 * Returns API username.
	*/
	public static String getUsername() {

		return get("username");
	}

	/**
	 * Returns API password.
	*/
	public static String getPassword() {

		return get("password");
	}

	/**
	 * Returns request timeout.
	*/
	public static int getRequestTimeout() {

		return Integer.parseInt(get("request.timeout", "30000"));
	}

	/**
	 * Returns connection timeout.
	 */
	public static int getConnectionTimeout() {

		return Integer.parseInt(get("connection.timeout", "30000"));
	}

	/**
	 * Returns currently selected environment.
	 */
	public static String getEnvironment() {

		return System.getProperty("env", "qa");
	}
}
