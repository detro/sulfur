package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.factories.exceptions.MissingPathParamException;
import com.amazon.aiv.sulfur.factories.exceptions.MissingQueryParamException;
import org.apache.log4j.Logger;

import java.util.*;
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
    private static final String PATTERN_FORMAT_PATH_PARAM = "\\{!?(%s)\\}";
    private static final String PATTERN_FORMAT_PATH_MANDATORY_PARAM = "\\{!(%s)\\}";

    /**
     * Extract Parameters from the Path, based on a given Pattern String.
     * The Parameters are portion of a URL Path, surrounded by curly brackets.
     * Example: "/example/path/{param1}/and/{param2}".
     *
     * @see PageConfig#PATTERN_FORMAT_PATH_PARAM
     * @see PageConfig#PATTERN_FORMAT_PATH_MANDATORY_PARAM
     *
     * @param patternStr String containing the Pattern that will be used to instantiate an actual Pattern object
     * @return A Set containing the Parameter found.
     */
    private LinkedHashSet<String> extractPathParams(String patternStr) {
        LinkedHashSet<String> paramsSet = new LinkedHashSet<String>();

        // Extract all the Path Mandatory Parameters and store them locally
        Matcher pathMandatoryParamsMatcher = Pattern.compile(String.format(patternStr, "\\w+")).matcher(getPath());
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Set<String> getQueryParams() {
        if (null == queryMandatoryParams) {
            initQueryMandatoryParams();
        }
        return queryParams;
    }

    public Set<String> getQueryMandatoryParams() {
        if (null == queryMandatoryParams) {
            initQueryMandatoryParams();
        }
        return queryMandatoryParams;
    }

    public Set<String> getPathParams() {
        if (null == pathParams) {
            pathParams = extractPathParams(PATTERN_FORMAT_PATH_PARAM);
        }
        return pathParams;
    }

    public Set<String> getPathMandatoryParams() {
        if (null == pathMandatoryParams) {
            pathMandatoryParams = extractPathParams(PATTERN_FORMAT_PATH_MANDATORY_PARAM);
        }
        return pathMandatoryParams;
    }

    public void logDebug(Logger logger) {
        logger.debug("  filename: " + getFilename());
        logger.debug("  name: " + getName());
        logger.debug("  components: " + Arrays.toString(getComponents()));
        logger.debug("  path: " + getPath());
        logger.debug("  pathParams: " + getPathParams());
        logger.debug("  pathMandatoryParams: " + getPathMandatoryParams());
        logger.debug("  queryParams: " + getQueryParams());
        logger.debug("  queryMandatoryParams: " + getQueryMandatoryParams());
    }

    private void checkPathParams(Map<String, String> pathParams) {
        // Check all the Mandatory Path Parameters are provided
        for (String pathMandatoryParam : getPathMandatoryParams()) {
            if (!pathParams.containsKey(pathMandatoryParam)) {
                throw new MissingPathParamException(pathMandatoryParam);
            }
        }
    }

    private void checkQueryParams(Map<String, String> queryParams) {
        // Check all the Mandatory Query Parameters are provided
        for (String queryMandatoryParam : getQueryMandatoryParams()) {
            if (!queryParams.containsKey(queryMandatoryParam)) {
                throw new MissingQueryParamException(queryMandatoryParam);
            }
        }
    }

    public String composeUrlPath(Map<String, String> pathParams) {
        // Check all MANDATORY the Path Params are provided
        checkPathParams(pathParams);

        // If we reach here, everything is fine and we can compose the URL Path
        String resultPath = getPath();

        // Replace parameters in the PageConfig Path with the given value
        for (Map.Entry<String, String> pathParam : pathParams.entrySet()) {
            resultPath.replaceFirst(String.format(PATTERN_FORMAT_PATH_PARAM, pathParam.getKey()), pathParam.getValue());
        }
        // Replace all the remaining parameter (not set with the previous loop) with an empty string
        resultPath.replaceAll(String.format(PATTERN_FORMAT_PATH_PARAM, ""), "");

        return resultPath;
    }

    public String composeUrlQuery(Map<String, String> queryParams) {
        // Check all MANDATORY the Query Params are provided
        checkQueryParams(queryParams);

        StringBuilder resultQuery = new StringBuilder();

        Iterator<Map.Entry<String, String>> i = queryParams.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, String> queryParam = i.next();

            resultQuery.append(queryParam.getKey());
            resultQuery.append("=");
            resultQuery.append(queryParam.getValue());

            resultQuery.append(i.hasNext() ? "&" : "");
        }

        return resultQuery.toString();
    }
}
