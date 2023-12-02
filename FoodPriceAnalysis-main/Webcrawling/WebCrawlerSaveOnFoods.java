package Webcrawling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class WebCrawlerSaveOnFoods {

	private static List<WebElement> links = new ArrayList<>();
	private static Hashtable<String, String> url_Map = new Hashtable<>();

	/**
     * Perform web crawling for the website "https://www.saveonfoods.com/" using Chrome driver in Selenium.
     *
     * @param websiteUrl The URL of the website to crawl.
     * @return Hashtable of URLs with respect to the file store.
     */
    public static Hashtable<String, String> startWebCrawling(String websiteUrl) {

        File file = new File("drivers/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        driver.get(websiteUrl);
        WebElement web = driver.findElement(By.cssSelector("#modal-content > div.Header--o81wql.cokFUX > button"));
        web.click();
        web = driver.findElement(By.cssSelector("#pageHeader > section.HeaderDesktop--jmz4lp.iTGDyA > section:nth-child(2) > div > nav > div:nth-child(1) > button"));
        web.click();
        
//        links = driver.findElements(By.cssSelector("div[class*=\'MegaMenuContent--1aiuqx gtnKaJ\']a"));
//        List<String> url_List = WebCrawler.findHyperLinks(links);
        links = driver.findElements(By.cssSelector("div.MegaMenuHiddenCategoryListWrapper--11chaob.eFolqZ a"));
        List<String> url_List = WebCrawler.findHyperLinks(links);

        System.out.println("Links"+ links);
        int counter = 0;
        for (String url : url_List) {
            try {
                driver.navigate().to(url);
                String content = driver.getPageSource();
                // Store the main page
                url_Map.putAll(WebCrawler.createFile(counter++, url, content, "SaveOnFoods", "SaveOnFoodsHtml/"));
                
                List<WebElement> moreLinks = driver.findElements(By.cssSelector("[data-test-id='megaMenu-hamburgerMenu-testId']"));
                List<String> url_List1 = WebCrawler.findHyperLinks(moreLinks);
                for (String u : url_List1) {
                    driver.navigate().to(u);
                    content = driver.getPageSource();
                    // Store the page
                    url_Map.putAll(WebCrawler.createFile(counter++, u, content, "SaveOnFoods", "SaveOnFoodsHtml/"));

                    List<WebElement> moreLinks1 = driver.findElements(By.cssSelector("ul[class*= 'MenuListWrapper--1ge68p6 hFrzaB'] a"));
                    List<String> url_List2 = WebCrawler.findHyperLinks(moreLinks1);
                    for (String u1 : url_List2) {
                        driver.navigate().to(u1);
                        content = driver.getPageSource();
                        // Store the page
                        url_Map.putAll(WebCrawler.createFile(counter++, u1, content, "SaveOnFoods", "SaveOnFoodsHtml/"));
                    }
                }

                // If you wish to extract only a limited set of pages from the website
                if (counter >= 5) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        driver.quit();
        return url_Map;
    }
}
