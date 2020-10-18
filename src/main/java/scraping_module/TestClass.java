package scraping_module;

import org.jsoup.nodes.Element;


public class TestClass {

    public static void main(String[] args) {
        Scraper scraper = new Scraper();


        Element indexElement = scraper.searchIndexElementByName("ATX", scraper.indexList);
        String price = indexElement.select("td[class$='last']").text();

        System.out.println(price);
    }
}
