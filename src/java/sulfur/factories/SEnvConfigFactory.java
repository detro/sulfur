package sulfur.factories;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.log4j.Logger;
import sulfur.configs.SEnvConfig;
import sulfur.factories.exceptions.SEnvConfigsLocationNotProvidedException;
import sulfur.factories.exceptions.SInvalidEnvConfigException;
import sulfur.factories.exceptions.SInvalidEnvConfigsLocationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan De Marino
 *
 * #factory
 * #environment
 * #config
 *
 * This Factory is responsible to load SEnvConfig objects.
 *
 * It assumes that:
 * <ol>
 *     <li>{@link SEnvConfigFactory#SYSPROP_ENV_CONFIGS_DIR_PATH} is set to a directory</li>
 *     <li>The directory contains Sulfur Environment Config Files (i.e. <code>*.sulfur.env.json</code>)</li>
 * </ol>
 */
public class SEnvConfigFactory {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(SEnvConfigFactory.class);

    /** MANDATORY System Property to instruct Sulfur where to look for SEnv Configs files */
    public static final String SYSPROP_ENV_CONFIGS_DIR_PATH = "sulfur.env.configs";
    /** MANDATORY Extension that SEnv Config files have to use */
    public static final String EXTENSION_ENV_CONFIG_FILE = ".sulfur.env.json";

    private static SEnvConfigFactory singleton = null;

    /** Holds a reference to the Filename of EnvConfigs that have been found */
    private Map<String, SEnvConfig> mEnvConfigs = new HashMap<String, SEnvConfig>();

    /**
     * Factory method
     *
     * @return Singleton SEnvConfigFactory
     * @throws sulfur.factories.exceptions.SEnvConfigsLocationNotProvidedException
     */
    public synchronized static SEnvConfigFactory getInstance() {
        if (null == singleton) {
            singleton = new SEnvConfigFactory();
        }

        return singleton;
    }

    /**
     * Utility method to get rid of the SEnvConfigFactory Singleton Instance.
     * NOTE: Make sure you know what you are doing when using this.
     */
    public synchronized static void clearInstance() {
        singleton = null;
    }

    /**
     * Creates a SEnvConfigFactory, checking the given SEnvConfigFactory#SYSPROP_ENV_CONFIGS_DIR_PATH.
     *
     * @throws SEnvConfigsLocationNotProvidedException
     * @throws SInvalidEnvConfigsLocationException
     * @throws SInvalidEnvConfigException
     * @throws JsonSyntaxException
     */
    private SEnvConfigFactory() {
        // Read path to SEnvConfig Files directory
        String envConfigsDirPath = System.getProperties().getProperty(SYSPROP_ENV_CONFIGS_DIR_PATH);
        if (null == envConfigsDirPath) {           //< check validity
            throw new SEnvConfigsLocationNotProvidedException();
        }

        // Scan the given directory for files with extension EXTENSION_ENV_CONFIG_FILE
        File envConfigsDir = new File(envConfigsDirPath);
        File[] envConfigsFiles = envConfigsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File fileDir, String fileName) {
                return fileName.endsWith(EXTENSION_ENV_CONFIG_FILE);
            }
        });
        if (null == envConfigsFiles || envConfigsFiles.length == 0) {             //< check validity
            throw new SInvalidEnvConfigsLocationException();
        }

        Gson gson = new Gson();
        for (int i = envConfigsFiles.length -1; i >= 0; --i) {
            LOG.debug("FOUND SEnvConfig file: " + envConfigsFiles[i].getPath());

            try {
                FileReader fReader = new FileReader(envConfigsFiles[i]);
                SEnvConfig pc = gson.fromJson(fReader, SEnvConfig.class);

                // Set the "filename" on the SEnvConfig
                pc.setFilename(envConfigsFiles[i].getName());

                // Ask the SEnvConfig to log itself
                pc.logDebug(LOG);

                // Store the new SEnvConfig
                mEnvConfigs.put(pc.getName(), pc);
            } catch (FileNotFoundException fnfe) {
                LOG.error("INVALID SEnvConfig (not found)");
                throw new SInvalidEnvConfigException(envConfigsFiles[i].getPath());
            } catch (JsonSyntaxException jse) {
                LOG.error("INVALID SEnvConfig (malformed)");
                throw jse;
            }
        }
    }

    /**
     * Return a SEnvConfig if found, null otherwise.
     *
     * @param name Name of the required SEnvConfig
     * @return A SEnvConfig or null
     */
    public SEnvConfig getEnvConfig(String name) {
        return mEnvConfigs.get(name);
    }

    /**
     * Return all the SEnvConfig the factory could find
     *
     * @return All the SEnvConfig objects in a map (key is the page name)
     */
    public Map<String, SEnvConfig> getEnvConfigs() {
        return mEnvConfigs;
    }
}
