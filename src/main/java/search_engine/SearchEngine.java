package search_engine;

import java.net.URL;
import java.util.List;

public interface SearchEngine {
    List<URL> search(String query, int limit);
}
