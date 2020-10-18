package scraping_module;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Scraper {

//    основные мировые индексы
//    https://ru.investing.com/indices/world-indices?&majorIndices=on
    public static final String MAJOR_INDICES = "https://ru.investing.com/indices/world-indices?&majorIndices=on";
    public static final String CSS_SELECTOR_INDEX_LIST = "tr[id^='pair_']";

    Document doc;
    Elements indexList;

    public Scraper() {
        try {
            doc = getDocument(MAJOR_INDICES);
            indexList = doc.select(CSS_SELECTOR_INDEX_LIST);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Document getDocument(String url) throws IOException{
        Document doc = Jsoup.connect(url)
                .userAgent("Chrome/4.0.249.0 Safari/532.5")
                .get();
        return doc;
    }

    //    Elements indexList = new Elements();



//    public static void main(String[] args) {
//        Scraper scraper = new Scraper();
//        Document doc;
//        Elements indexList = new Elements();
//
//        try {
//            doc = scraper.getDocument(MAJOR_INDICES);
//            indexList = doc.select("tr[id^='pair_']");
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Element indexElement = scraper.searchIndexElementByName("ATX", indexList);
//        String price = indexElement.select("td[class$='last']").text();
//    }



    public Element searchIndexElementByName(String indexName, Elements elements) {
        for (Element element: elements) {
            String name = element.select("td a").attr("title");
            if (indexName.equals(name))
                return element;
        }
        return null;
    }
}
