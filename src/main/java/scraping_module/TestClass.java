package scraping_module;

import org.jsoup.nodes.Element;
import scraping_module.exceptions.IndexNotFoundException;


public class TestClass {

    public static void main(String[] args) throws IndexNotFoundException {
        Scraper scraper = new Scraper();


        Element indexElement = scraper.searchIndexElementByName("ATX");
        String price = indexElement.select("td[class$='last']").text();

        System.out.println(price);
    }
}
