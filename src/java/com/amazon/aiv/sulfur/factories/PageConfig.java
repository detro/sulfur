package com.amazon.aiv.sulfur.factories;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a POJO, intended to be used with Google GSON.
 *
 * @author Ivan De Marino <demarino@amazon.com>
 *
 * #pojo
 * #json
 * #gson
 * #struct
 * #page
 * #config
 *
 * TODO
 */
public class PageConfig {
    // JSON
    private String              name = null;
    private String[]            components = null;
    private String              path = null;
    private String[]            queryParams = null;

    // Derived
    private transient String    filename = null;
    private transient String[]  pathParams = null;
    private transient String[]  pathMandatoryParams = null; //< might seem redundant, but it's unlikely to be a long array
    private transient String[]  queryMandatoryParams = null;

    // Patterns to extract Parameters from Path
    private static final String PATTERN_PATH_PARAM = "\\{!?(\\w+)\\}";
    private static final String PATTERN_PATH_MANDATORY_PARAM = "\\{!(\\w+)\\}";

    private String[] extractPathParams(String patternStr) {
        List<String> paramsList = new ArrayList<String>();

        // Extract all the Path Mandatory Parameters and store them locally
        Matcher pathMandatoryParamsMatcher = Pattern.compile(patternStr).matcher(getPath());
        while (pathMandatoryParamsMatcher.find()) {
            paramsList.add(pathMandatoryParamsMatcher.group(1));
        }
        return paramsList.toArray(new String[paramsList.size()]);
    }

    private void initQueryMandatoryParams() {
        List<String> paramsList = new ArrayList<String>();
        for (int i = queryParams.length -1; i >= 0; --i) {
            if (queryParams[i].startsWith("!")) {
                queryParams[i] = queryParams[i].replace("!", "");
                paramsList.add(queryParams[i]);
            }
        }

        queryMandatoryParams = paramsList.toArray(new String[paramsList.size()]);
    }

    public String getName() {
        return null == name ? filename.replace(Consts.EXTENSION_PAGE_CONFIG_FILE, "") : name;
    }

    public String[] getComponents() {
        return components;
    }

    public String getPath() {
        return path;
    }

    public String[] getQueryParams() {
        if (null == queryMandatoryParams) {
            initQueryMandatoryParams();
        }
        return queryParams;
    }

    public String[] getQueryMandatoryParams() {
        if (null == queryMandatoryParams) {
            initQueryMandatoryParams();
        }
        return queryMandatoryParams;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String[] getPathParams() {
        if (null == pathParams) {
            pathParams = extractPathParams(PATTERN_PATH_PARAM);
        }
        return pathParams;
    }

    public String[] getPathMandatoryParams() {
        if (null == pathMandatoryParams) {
            pathMandatoryParams = extractPathParams(PATTERN_PATH_MANDATORY_PARAM);
        }
        return pathMandatoryParams;
    }

    public void logDebug(Logger logger) {
        logger.debug("  filename: " + getFilename());
        logger.debug("  name: " + getName());
        logger.debug("  components: " + Arrays.toString(getComponents()));
        logger.debug("  path: " + getPath());
        logger.debug("  pathParams: " + Arrays.toString(getPathParams()));
        logger.debug("  pathMandatoryParams: " + Arrays.toString(getPathMandatoryParams()));
        logger.debug("  queryParams: " + Arrays.toString(getQueryParams()));
        logger.debug("  queryMandatoryParams: " + Arrays.toString(getQueryMandatoryParams()));
    }
}
