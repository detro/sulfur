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

package sulfur.factories;

/**
 * @author Ivan De Marino
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
