package com.amazon.aiv.sulfur.factories;

import com.amazon.aiv.sulfur.factories.exceptions.InvalidDriverNameException;
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
public class WebDriverFactory {

    private WebDriverFactory() {
        // This class can't be instantiated
    }

    public static WebDriver createDriver(String driverName) {
        switch(driverName) {
            case Consts.DRIVERNAME_FIREFOX:
                return createFirefoxDriver();
            case Consts.DRIVERNAME_CHROME:
                return createChromeDriver();
            case Consts.DRIVERNAME_IE:
                return createIEDriver();
            case Consts.DRIVERNAME_PHANTOMJS:
                return createPhantomJSDriver();
            case Consts.DRIVERNAME_OPERA:
                return createOperaDriver();
            default:
                throw new InvalidDriverNameException(driverName);
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
