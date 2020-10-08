package org.combinator.demo;


import org.combinator.demo.constants.MessagesConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class CombinatorApplication {

    public static final Logger logger = LoggerFactory.getLogger(CombinatorApplication.class.getName());

    Properties applicationProperties;
    WebDriver webDriver;

    public CombinatorApplication(WebDriver webDriver, Properties properties){
        this.applicationProperties = properties;
        this.webDriver = webDriver;
    }

    public static void main(String[] args) {
        WebDriver webDriver = null;
        ChromeOptions chromeOptions = null;
        Properties properties = null;
        CombinatorApplication combinatorApplication = null;
        try{
            properties = initializeProperties();
            String driverUrl = properties.getProperty("application.selenium.url");
            webDriver = initializeWebDriver(driverUrl);
        }catch (MalformedURLException malformedURLException){
            logger.error(MessagesConstants.ERROR_CODE_MALFORMED_URL,malformedURLException);
            System.exit(1);
        } catch (IOException e) {
            logger.error(MessagesConstants.ERROR_CODE_IO_EXCEPTION,e);
            System.exit(1);
        } catch (Exception e){
            logger.error("Exception Occurred",e);
        }

        combinatorApplication = new CombinatorApplication(webDriver, properties);

    }

    private static WebDriver initializeWebDriver(String remoteDriverUrl) throws MalformedURLException{
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors","--disable-dev-shm-usage");
        WebDriver webDriver = new RemoteWebDriver(new URL(remoteDriverUrl),chromeOptions);
        return webDriver;
    }

    private static Properties initializeProperties() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = CombinatorApplication.class.getClass().getResourceAsStream("/application.properties");
        properties.load(inputStream);
        return properties;
    }



}
