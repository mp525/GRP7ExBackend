package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.FilmDTO;
import dto.RawFilmDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import utils.HttpUtils;

public class FetchFacade {

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
    
    public List<FilmDTO> fetchReviewByTitle(String title) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<FilmDTO> films = new ArrayList();
        String url = "https://api.nytimes.com/svc/movies/v2/reviews/search.json?api-key=5tN35qLGRRkvgYSCFj7wKdwhDNb5PMOF" + "&query=" + title;
        String fetch = HttpUtils.fetchData(url);
        RawFilmDTO film = gson.fromJson(fetch, RawFilmDTO.class);
        for(FilmDTO f : film.getResults()) {
            films.add(f);
        }
        return films;
    }
    
    public static void main(String[] args) throws IOException {
        FetchFacade facade = new FetchFacade();
        System.out.println(facade.fetchReviewByTitle("harry potter"));
        System.out.println(facade.fetchReviewByTitle("lebowski"));
    }

}
