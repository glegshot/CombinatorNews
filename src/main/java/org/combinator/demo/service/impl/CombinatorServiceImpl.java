package org.combinator.demo.service.impl;

import org.combinator.demo.service.CombinatorService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Properties;

public class CombinatorServiceImpl implements CombinatorService {

    Properties applicationProperties;
    WebDriver webDriver;

    public CombinatorServiceImpl(WebDriver webDriver, Properties properties){
        this.applicationProperties = properties;
        this.webDriver = webDriver;
    }

    @Override
    public void scrapeData() {
        int maxPageToScrape = Integer.parseInt(applicationProperties.getProperty("application.maxPageToScrap"));
        String baseUrl = applicationProperties.getProperty("application.baseUrl");
        String requestPath = applicationProperties.getProperty("application.requestPath");

        for(int i = 1; i<=maxPageToScrape;i++){

            String requestURL = getRequestURL(baseUrl,requestPath,i);
            webDriver.get(requestURL);
            extractPageElements();
        }

    }

    public void extractPageElements(){
        List<WebElement> newsItems = this.webDriver.findElements(By.xpath("//tr[@class='athing']/td[not(@align)][@class='title']"));
        for(WebElement newsItem : newsItems){
            System.out.println("title: "+newsItem.getText());
            WebElement comment = newsItem.findElement(By.xpath("./following::tr/td[@class='subtext']/a[contains(@href,'item')]"));
            System.out.println("comment: "+comment.getText());
            WebElement newsLink = newsItem.findElement(By.xpath("./a[@class='storylink']"));
            System.out.println("link: "+newsLink.getAttribute("href"));
            WebElement score = newsItem.findElement(By.xpath("//tr[@class='athing']/following::tr/td[@class='subtext']/span[@class='score']"));
            System.out.println("score: "+score.getText());
        }


    }

    private String getRequestURL(String baseUrl, String requestPath, int currentPageNumber){
        String queryParamString = "?p=";
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(baseUrl);
        urlStringBuilder.append(requestPath);
        urlStringBuilder.append(queryParamString);
        urlStringBuilder.append(currentPageNumber);
        return urlStringBuilder.toString();
    }

}


