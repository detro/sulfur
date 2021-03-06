/*
This file is part of the Sulfur project by Ivan De Marino (http://ivandemarino.me).

Copyright (c) 2013, Ivan De Marino (http://ivandemarino.me)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package sulfur.configs;

import org.apache.log4j.Logger;
import sulfur.factories.SPageConfigFactory;
import sulfur.factories.exceptions.SMissingPathParamException;
import sulfur.factories.exceptions.SMissingQueryParamException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a POJO, intended to be used with Google GSON.
 *
 * @author Ivan De Marino
 *
 * #pojo
 * #json
 * #gson
 * #struct
 * #page
 * #config
 *
 * TODO Write documentation
 */
public class SPageConfig {
    // JSON
    private String                  name = null;
    private LinkedHashSet<String>   components = null;  //< "LinkedHashSet" ensure we get O(1) search and guaranteed order
    private String                  path = null;
    private LinkedHashSet<String>   queryParams = null; //< "LinkedHashSet" ensure we get O(1) search and guaranteed order

    // Derived
    private transient String        filename = null;
    private transient Set<String>   pathParams = null;
    private transient Set<String>   pathMandatoryParams = null; //< might seem redundant, but it's unlikely to be a long array
    private transient Set<String>   queryMandatoryParams = null;
    private transient Set<String>   mandatoryComponents = null;

    // Patterns to extract Parameters from Path
    private static final String PATTERN_MANDATORY_PREFIX = "!";
    private static final String PATTERN_FORMAT_PATH_PARAM = "\\{!?(%s)\\}";
    private static final String PATTERN_FORMAT_PATH_MANDATORY_PARAM = "\\{!(%s)\\}";

    /**
     * Extract Parameters from the Path, based on a given Pattern String.
     * The Parameters are portion of a URL Path, surrounded by curly brackets.
     * Example: "/example/path/{param1}/and/{param2}".
     *
     * @see SPageConfig#PATTERN_FORMAT_PATH_PARAM
     * @see SPageConfig#PATTERN_FORMAT_PATH_MANDATORY_PARAM
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
     * Extracts a Set of Mandatory entries from an initial String Set.
     * The Strings that are prefixed with PATTERN_MANDATORY_PREFIX (ex. "!"), are the ones considered Mandatory.
     *
     * NOTE: Mandatory strings are updated to remove the PATTERN_MANDATORY_PREFIX, in both the initial and result set.
     *
     * @param initial Initial String Set to do the extraction from
     * @return String Set with the Mandatory strings
     */
    private static Set<String> extractMandatoryStringSet(Set<String> initial) {
        // Fill the "mandatory" set
        Set<String> mandatory = new LinkedHashSet<String>();
        for (String iEntry : initial) {
            if (iEntry.startsWith(PATTERN_MANDATORY_PREFIX)) {
                mandatory.add(iEntry.replaceFirst(PATTERN_MANDATORY_PREFIX, ""));
            }
        }

        // Replace "mandatory" entries in the "initial" set with the same string minus the PATTERN_MANDATORY_PREFIX
        for (String mEntry : mandatory) {
            initial.remove(PATTERN_MANDATORY_PREFIX + mEntry);
            initial.add(mEntry);
        }

        return mandatory;
    }

    /**
     * Initializes the list of Query Mandatory Parameters.
     * This will take care of also removing the prefix PATTERN_MANDATORY_PREFIX in front of the Mandatory parameters.
     */
    private void initQueryMandatoryParams() {
        if (null == queryMandatoryParams) {
            queryMandatoryParams = extractMandatoryStringSet(queryParams);
        }
    }

    private void initMandatoryComponents() {
        if (null == mandatoryComponents) {
            mandatoryComponents = extractMandatoryStringSet(components);
        }
    }

    public String getName() {
        return null == name ? filename.replace(SPageConfigFactory.EXTENSION_PAGE_CONFIG_FILE, "") : name;
    }

    public Set<String> getComponentClassnames() {
        initMandatoryComponents();
        return components;
    }

    public Set<String> getMandatoryComponentClassnames() {
        initMandatoryComponents();
        return mandatoryComponents;
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
        initQueryMandatoryParams();
        return queryParams;
    }

    public Set<String> getQueryMandatoryParams() {
        initQueryMandatoryParams();
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
        logger.debug("  components: " + getComponentClassnames());
        logger.debug("  mandatoryComponents: " + getMandatoryComponentClassnames());
        logger.debug("  path: " + getPath());
        logger.debug("  pathParams: " + getPathParams());
        logger.debug("  pathMandatoryParams: " + getPathMandatoryParams());
        logger.debug("  queryParams: " + getQueryParams());
        logger.debug("  queryMandatoryParams: " + getQueryMandatoryParams());
    }

    private void checkPathParams(Map<String, String> pathParams) {
        // Check all the Mandatory Path Parameters are provided
        for (String pathMandatoryParam : getPathMandatoryParams()) {
            if (null == pathParams || !pathParams.containsKey(pathMandatoryParam)) {
                throw new SMissingPathParamException(pathMandatoryParam);
            }
        }
    }

    private void checkQueryParams(Map<String, String> queryParams) {
        // Check all the Mandatory Query Parameters are provided
        for (String queryMandatoryParam : getQueryMandatoryParams()) {
            if (null == queryParams || !queryParams.containsKey(queryMandatoryParam)) {
                throw new SMissingQueryParamException(queryMandatoryParam);
            }
        }
    }

    public String composeUrlPath(Map<String, String> pathParams) {
        // Check all MANDATORY the Path Params are provided
        checkPathParams(pathParams);

        // If we reach here, everything is fine and we can compose the URL Path
        String resultPath = getPath();

        // Replace parameters in the SPageConfig Path with the given value
        if (null != pathParams) {
            for (Map.Entry<String, String> pathParam : pathParams.entrySet()) {
                resultPath = resultPath.replaceFirst(
                        String.format(PATTERN_FORMAT_PATH_PARAM, pathParam.getKey()),
                        pathParam.getValue());
            }
        }
        // Replace all the remaining parameter (not set with the previous loop) with an empty string
        resultPath = resultPath.replaceAll(String.format(PATTERN_FORMAT_PATH_PARAM, "\\w+"), "");

        return resultPath;
    }

    public String composeUrlQuery(Map<String, String> queryParams) {
        // Check all MANDATORY the Query Params are provided
        checkQueryParams(queryParams);

        StringBuilder resultQuery = new StringBuilder();

        if (null != queryParams) {
            Iterator<Map.Entry<String, String>> i = queryParams.entrySet().iterator();
            if (i.hasNext()) {
                resultQuery.append("?");
            }
            while (i.hasNext()) {
                Map.Entry<String, String> queryParam = i.next();

                resultQuery.append(queryParam.getKey());
                resultQuery.append("=");
                resultQuery.append(queryParam.getValue());

                resultQuery.append(i.hasNext() ? "&" : "");
            }
        }

        return resultQuery.toString();
    }

    @Override
    public String toString() {
        return String.format("Env '%s' ('%s')", getName(), getFilename());
    }
}
