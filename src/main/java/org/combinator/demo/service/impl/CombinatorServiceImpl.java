package org.combinator.demo.service.impl;

import org.combinator.demo.dto.CombinatorNews;
import org.combinator.demo.service.CombinatorService;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class CombinatorServiceImpl implements CombinatorService {

    public static final Logger logger = LoggerFactory.getLogger(CombinatorService.class.getName());

    Properties applicationProperties;
    WebDriver webDriver;

    String newsItemXpath;
    String newsCommentTotalXpath;
    String newsLinkXpath;
    String newsScoreXpath;
    String moreLink;

    boolean lastPage;

    public CombinatorServiceImpl(WebDriver webDriver, Properties properties) {
        this.applicationProperties = properties;
        this.webDriver = webDriver;
        this.newsItemXpath = applicationProperties.getProperty("application.xpaths.newsItem");
        this.newsCommentTotalXpath = applicationProperties.getProperty("application.xpaths.newsCommentTotal");
        this.newsLinkXpath = applicationProperties.getProperty("application.xpaths.newsLink");
        this.newsScoreXpath = applicationProperties.getProperty("application.xpaths.newsScore");
        this.moreLink = applicationProperties.getProperty("application.xpaths.moreLink");
        this.lastPage = false;
    }

    @Override
    public void scrapeData() {
        int maxPageToScrape = Integer.parseInt(applicationProperties.getProperty("application.maxPageToScrap"));
        String baseUrl = applicationProperties.getProperty("application.baseUrl");
        String requestPath = applicationProperties.getProperty("application.requestPath");

        List<CombinatorNews> newsList = new ArrayList<>();

        logger.info("max pages to scrape :  " + maxPageToScrape);

        for (int i = 1; i <= maxPageToScrape; i++) {

            String requestURL = getRequestURL(baseUrl, requestPath, i);
            logger.info("GET request for url  : " + requestURL);
            webDriver.get(requestURL);
            isLastPage();
            webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            extractPageElements(newsList);
            if(this.lastPage){
                logger.info("last page : "+requestURL);
                break;
            }

        }
        logger.info("total news list size : " + newsList.size());

    }

    public void extractPageElements(List newsList) {

        List<WebElement> newsItems = this.webDriver.findElements(By.xpath(this.newsItemXpath));
        for (WebElement newsItem : newsItems) {
            String newsTitle = newsItem.getText();
            WebElement commentTotalNumberElement = newsItem.findElement(By.xpath(this.newsCommentTotalXpath));
            String newsTotalComment = commentTotalNumberElement.getText();
            WebElement newsLinkElement = newsItem.findElement(By.xpath(this.newsLinkXpath));
            String newsLink = newsLinkElement.getAttribute("href");
            WebElement scoreElement = newsItem.findElement(By.xpath(this.newsScoreXpath));
            String newsScore = scoreElement.getText();

            CombinatorNews combinatorNews = new CombinatorNews(newsTitle, newsLink, newsTotalComment, newsScore);
            newsList.add(combinatorNews);
        }

    }

    private void isLastPage(){
        try {
            webDriver.findElement(By.xpath(this.moreLink));
        }catch(NoSuchElementException e){
            this.lastPage = true;
        }
    }

    private String getRequestURL(String baseUrl, String requestPath, int currentPageNumber) {
        String queryParamString = "?p=";
        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append(baseUrl);
        urlStringBuilder.append(requestPath);
        urlStringBuilder.append(queryParamString);
        urlStringBuilder.append(currentPageNumber);
        return urlStringBuilder.toString();
    }

}


