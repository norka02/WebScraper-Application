package pl.hubertmaka.project.interfaces;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import java.util.ArrayList;

public interface Scraper {
    Elements scrapSite(int page);
    Elements getItemsList(Document document);
    Document getDocument(Connection connection);
    String buildUrl(int page);
    Connection connectToSite(String url);
    ArrayList<Elements> getAllElementsFromSite();
    ArrayList<Elements> getAllElementsFromSite(int max_pages);
}
