package com.amazon.aiv.sulfur.factories;

/**
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * #const
 */
public interface Consts {
    /** MANDATORY System Property to instruct Sulfur where to look for Page Configs files */
    public static final String SYSPROP_PAGE_CONFIGS_DIR_PATH    = "sulfur.page.configs";
    /** MANDATORY Extension that Page Config files have to use */
    public static final String EXTENSION_PAGE_CONFIG_FILE = ".sulfur.page.config.json";

    /** MANDATORY System Property to instruct Sulfur where to look for the Config file */
    public static final String SYSPROP_CONFIG_FILE_PATH = "sulfur.config";
}
