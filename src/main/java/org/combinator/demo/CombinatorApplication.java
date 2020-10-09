package org.combinator.demo;


import org.combinator.demo.constants.MessagesConstants;
import org.combinator.demo.service.CombinatorService;
import org.combinator.demo.service.impl.CombinatorServiceImpl;
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

    public static void main(String[] args) {
        WebDriver webDriver = null;
        ChromeOptions chromeOptions = null;
        Properties properties = null;

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
            System.exit(1);
        }

        try {
            CombinatorService combinatorService = new CombinatorServiceImpl(webDriver, properties);
            combinatorService.scrapeData();
        }catch(Exception e){
            logger.error("Exception Occured", e);
        }finally{
            if(webDriver != null){
                webDriver.close();
                webDriver.quit();
            }
        }
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
