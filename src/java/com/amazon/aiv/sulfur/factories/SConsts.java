package com.amazon.aiv.sulfur.factories;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * #const
 */
public interface SConsts {
    /** MANDATORY System Property to instruct Sulfur where to look for SPage Configs files */
    public static final String SYSPROP_PAGE_CONFIGS_DIR_PATH    = "sulfur.page.configs";
    /** MANDATORY System Property to instruct Sulfur where to look for the Config file */
    public static final String SYSPROP_CONFIG_FILE_PATH = "sulfur.config";

    /** MANDATORY Extension that SPage Config files have to use */
    public static final String EXTENSION_PAGE_CONFIG_FILE = ".sulfur.page.config.json";

    public static final String DRIVERNAME_FIREFOX = "firefox";
    public static final String DRIVERNAME_CHROME = "chrome";
    public static final String DRIVERNAME_IE = "ie";
    public static final String DRIVERNAME_PHANTOMJS = "phantomjs";
    public static final String DRIVERNAME_OPERA = "opera";
}
