package com.amazon.aiv.sulfur.factories;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
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
    private String                  name = null;
    private String[]                components = null;
    private String                  path = null;
    private LinkedHashSet<String>   queryParams = null;

    // Derived
    private transient String                    filename = null;
    private transient LinkedHashSet<String>     pathParams = null;
    private transient LinkedHashSet<String>     pathMandatoryParams = null; //< might seem redundant, but it's unlikely to be a long array
    private transient LinkedHashSet<String>     queryMandatoryParams = null;

    // Patterns to extract Parameters from Path
    private static final String PATTERN_PATH_PARAM = "\\{!?(\\w+)\\}";
    private static final String PATTERN_PATH_MANDATORY_PARAM = "\\{!(\\w+)\\}";

    /**
     * Extract Parameters from the Path, based on a given Pattern String.
     * The Parameters are portion of a URL Path, surrounded by curly brackets.
     * Example: "/example/path/{param1}/and/{param2}".
     *
     * @see PageConfig#PATTERN_PATH_PARAM
     * @see PageConfig#PATTERN_PATH_MANDATORY_PARAM
     *
     * @param patternStr String containing the Pattern that will be used to instantiate an actual Pattern object
     * @return A Set containing the Parameter found.
     */
    private LinkedHashSet<String> extractPathParams(String patternStr) {
        LinkedHashSet<String> paramsSet = new LinkedHashSet<String>();

        // Extract all the Path Mandatory Parameters and store them locally
        Matcher pathMandatoryParamsMatcher = Pattern.compile(patternStr).matcher(getPath());
        while (pathMandatoryParamsMatcher.find()) {
            paramsSet.add(pathMandatoryParamsMatcher.group(1));
        }

        return paramsSet;
    }

    /**
     * Initializes the list of Query Mandatory Parameters.
     * This will take care of also removing the prefix "!" in front of the Mandatory parameters.
     */
    private void initQueryMandatoryParams() {
        // Fill the "queryMandatoryParams" Set
        queryMandatoryParams = new LinkedHashSet<String>();
        for (String queryParam : queryParams) {
            if (queryParam.startsWith("!")) {
                queryMandatoryParams.add(queryParam.replaceFirst("!", ""));
            }
        }

        // Replace mandatory params in "queryParams" with the same parameter minus the "!" prefix
        for (String queryMandatoryParam : queryMandatoryParams) {
            queryParams.remove("!" + queryMandatoryParam);
            queryParams.add(queryMandatoryParam);
        }
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

    public Set<String> getQueryParams() {
        if (null == queryMandatoryParams) {
            initQueryMandatoryParams();
        }
        return queryParams;
    }

    public Set<String> getQueryMandatoryParamsSet() {
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

    public Set<String> getPathParamsSet() {
        if (null == pathParams) {
            pathParams = extractPathParams(PATTERN_PATH_PARAM);
        }
        return pathParams;
    }

    public Set<String> getPathMandatoryParamsSet() {
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
        logger.debug("  pathParams: " + getPathParamsSet());
        logger.debug("  pathMandatoryParams: " + getPathMandatoryParamsSet());
        logger.debug("  queryParams: " + getQueryParams());
        logger.debug("  queryMandatoryParams: " + getQueryMandatoryParamsSet());
    }
}
