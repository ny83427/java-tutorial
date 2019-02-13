import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class OfferHunter {

    private static void printOfferPrices(Document doc) {
        Elements offers = doc.select("div.olpOffer");
        for (Element offer : offers) {
            System.out.println(offer.select(".olpOfferPrice").text());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final String url = "https://www.amazon.com/gp/offer-listing/0310438926/ref=tmm_hrd_new_olp_sr?ie=UTF8&condition=new&qid=1550083298&sr=8-3";
        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.96 Safari/537.36").get();
        printOfferPrices(doc);

        // replace with a valid path, or can you enhance it to download and unzip
        // chromedriver file automatically if it doesn't exist?
        System.setProperty("webdriver.chrome.driver", "C:\\Tmp\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.get(url);
        Thread.sleep(2000);
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("NIV");

        doc = Jsoup.parse(driver.getPageSource());
        printOfferPrices(doc);

        driver.quit();
    }
}
