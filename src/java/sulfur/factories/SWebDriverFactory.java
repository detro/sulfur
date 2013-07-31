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

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import sulfur.factories.exceptions.SInvalidConfigException;
import sulfur.factories.exceptions.SInvalidDriverNameException;

import java.io.FileNotFoundException;

/**
 * @author Ivan De Marino - detronizator@gmail.com
 *
 * TODO
 */
public class SWebDriverFactory {
    private static final Logger LOG = Logger.getLogger(SWebDriverFactory.class);

    public static final String DRIVERNAME_FIREFOX = BrowserType.FIREFOX;
    public static final String DRIVERNAME_CHROME = BrowserType.CHROME;
    public static final String DRIVERNAME_IE = BrowserType.IE;
    public static final String DRIVERNAME_PHANTOMJS = BrowserType.PHANTOMJS;
    public static final String DRIVERNAME_OPERA = BrowserType.OPERA;

    private static SWebDriverFactory singleton = null;

    private final SConfig mSulfurConfig;

    private SWebDriverFactory(SConfig sulfurConfig) {
        mSulfurConfig = sulfurConfig;
    }

    /**
     * Factory Method
     *
     * @return The SWebDriverFactory
     */
    public synchronized static SWebDriverFactory getInstance() {
        if (null == singleton) {
            try {
                singleton = new SWebDriverFactory(SConfig.getConfig());
            } catch (FileNotFoundException fnfe) {
                LOG.error("INVALID Config (not found)");
                throw new SInvalidConfigException(fnfe.getMessage());
            }
        }

        return singleton;
    }

    /**
     * Utility method to get rid of the SWebDriverFactory Singleton Instance.
     * NOTE: Make sure you know what you are doing when using this.
     */
    public synchronized static void clearInstance() {
        singleton = null;
    }

    public WebDriver createDriver(String driverName) {
        LOG.debug("Creating Driver: " + driverName);

        // Either we ask a Selenium HUB for the Driver or we run it locally
        if (null != mSulfurConfig.getSeleniumHub()) {
            LOG.debug(String.format("Driver will be requested to Selenium HUB at '%s'",
                    mSulfurConfig.getSeleniumHub().toString()));

            // Figure out which capability to use
            DesiredCapabilities caps;
            switch(driverName) {
                case DRIVERNAME_FIREFOX:
                    caps = DesiredCapabilities.firefox();
                    break;
                case DRIVERNAME_CHROME:
                    caps = DesiredCapabilities.chrome();
                    break;
                case DRIVERNAME_IE:
                    caps = DesiredCapabilities.internetExplorer();
                    break;
                case DRIVERNAME_PHANTOMJS:
                    caps = DesiredCapabilities.phantomjs();
                    break;
                case DRIVERNAME_OPERA:
                    caps = DesiredCapabilities.opera();
                    break;
                default:
                    caps = new DesiredCapabilities();
                    break;
            }

            return new RemoteWebDriver(mSulfurConfig.getSeleniumHub(), caps);
        } else {
            // Figure out which browser to use
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
    }

    public WebDriver createFirefoxDriver() {
        // TODO - too basic at the moment
        return new FirefoxDriver(DesiredCapabilities.firefox());
    }

    public WebDriver createChromeDriver() {
        // TODO - too basic at the moment
        return new ChromeDriver(DesiredCapabilities.chrome());
    }

    public WebDriver createIEDriver() {
        // TODO - too basic at the moment
        return new InternetExplorerDriver(DesiredCapabilities.internetExplorer());
    }

    public WebDriver createOperaDriver() {
        // TODO
        return null;
    }

    public WebDriver createPhantomJSDriver() {
        // TODO - too basic at the moment
        return new PhantomJSDriver(DesiredCapabilities.phantomjs());
    }
}
