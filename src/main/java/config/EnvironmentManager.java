package config;

public final class EnvironmentManager {

    private EnvironmentManager() {
        // Prevent object creation
    }

    /**
     * Supported environments.
     */
    public enum Environment {

        QA("qa"),
        STAGE("stage"),
        PROD("prod");

        private final String value;

        Environment(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    
    public static Environment getEnvironment() {
        String environment = System.getProperty("env", "qa");
        
        // Convert to lowercase once to avoid repeating it in conditions
        String envLower = environment.toLowerCase(); 
        
        if (envLower.equals("qa")) {
            return Environment.QA;
            
        } else if (envLower.equals("stage") || envLower.equals("staging")) {
            return Environment.STAGE;
            
        } else if (envLower.equals("prod") || envLower.equals("production")) {
            return Environment.PROD;
            
        } else {
            throw new IllegalArgumentException(
                "Unsupported environment: " + environment + ". Supported values: qa, stage, prod"
            );
        }
    }

    
    
    /**
     * Returns environment name.
     */
    public static String getEnvironmentName() {

        return getEnvironment().getValue();
    }

    /**
     * Checks whether current environment is QA.
     */
    public static boolean isQA() {

        return getEnvironment() == Environment.QA;
    }

    /**
     * Checks whether current environment is STAGE.
     */
    public static boolean isStage() {

        return getEnvironment() == Environment.STAGE;
    }

    /**
     * Checks whether current environment is PROD.
     */
    public static boolean isProd() {

        return getEnvironment() == Environment.PROD;
    }
}
