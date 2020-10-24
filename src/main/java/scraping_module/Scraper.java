package scraping_module;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scraping_module.exceptions.IndexNotFoundException;

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

    public String getPrice(Element index) {
        if (index != null)
            return  index.select("td[class$='last']").text();
        else
            return "";
    }



    public Element searchIndexElementByName(String indexName) throws IndexNotFoundException {
        for (Element element: indexList) {
            String name = element.select("td a").text();
            if (indexName.equalsIgnoreCase(name))
                return element;
        }
        throw new IndexNotFoundException("Selected index not found");
    }
}
