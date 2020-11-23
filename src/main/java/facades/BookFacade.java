package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.BookDTO;
import dto.RawBookDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import utils.HttpUtils;

public class BookFacade {

    class Default implements Callable<String> {

        String url;

        Default(String url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            String raw = HttpUtils.fetchData(url);
            return raw;
        }
    }
    
    public List<BookDTO> fetchBooks(String title) throws IOException{
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<BookDTO> books = new ArrayList();
        String url = "https://api.nytimes.com/svc/books/v3/reviews.json?api-key=5tN35qLGRRkvgYSCFj7wKdwhDNb5PMOF" + "&title=" + title;
        String raw = HttpUtils.fetchData(url);
        RawBookDTO rawBook = gson.fromJson(raw, RawBookDTO.class);
        for (BookDTO result : rawBook.getResults()) {
            
            books.add(result);
        }
        return books;
    }

    public List<String> fetchParallel() throws InterruptedException, ExecutionException {
        String[] hostList = {"https://api.chucknorris.io/jokes/random", "https://icanhazdadjoke.com",
            "https://swapi.dev/api/planets/schema", "https://swapi.dev/api/vehicles/schema", "https://swapi.dev/api/species/schema"};
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();
        List<String> retList = new ArrayList();

        for (String url : hostList) {
            Callable<String> urlTask = new Default(url);
            Future future = executor.submit(urlTask);
            futures.add(future);
        }

        for (Future<String> fut : futures) {
            retList.add(fut.get());
        }
       
        return retList;
    }
    
    public static void main(String[] args) throws IOException {
        BookFacade facade = new BookFacade();
        
        System.out.println(facade.fetchBooks("1Q84"));
    }

}
