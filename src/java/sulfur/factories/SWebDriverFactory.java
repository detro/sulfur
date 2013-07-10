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

import sulfur.factories.exceptions.SInvalidDriverNameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Ivan De Marino - detronizator@gmail.com
 *
 * TODO
 */
public class SWebDriverFactory {

    public static final String DRIVERNAME_FIREFOX = "firefox";
    public static final String DRIVERNAME_CHROME = "chrome";
    public static final String DRIVERNAME_IE = "ie";
    public static final String DRIVERNAME_PHANTOMJS = "phantomjs";
    public static final String DRIVERNAME_OPERA = "opera";

    private SWebDriverFactory() {
        // This class can't be instantiated
    }

    public static WebDriver createDriver(String driverName) {
        switch(driverName) {
            case DRIVERNAME_FIREFOX:
                return createFirefoxDriver();
            case DRIVERNAME_CHROME:
                return createChromeDriver();
            case DRIVERNAME_IE:
                return createIEDriver();
            case DRIVERNAME_PHANTOMJS:
                return createPhantomJSDriver();
            case DRIVERNAME_OPERA:
                return createOperaDriver();
            default:
                throw new SInvalidDriverNameException(driverName);
        }
    }

    public static WebDriver createFirefoxDriver() {
        // TODO - too basic at the moment
        return new FirefoxDriver();
    }

    public static WebDriver createChromeDriver() {
        // TODO - too basic at the moment
        return new ChromeDriver();
    }

    public static WebDriver createIEDriver() {
        // TODO - too basic at the moment
        return new InternetExplorerDriver();
    }

    public static WebDriver createOperaDriver() {
        // TODO
        return null;
    }

    public static WebDriver createPhantomJSDriver() {
        // TODO - too basic at the moment
        return new PhantomJSDriver(DesiredCapabilities.phantomjs());
    }
}
