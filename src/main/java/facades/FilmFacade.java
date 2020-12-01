package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.FilmDTO;
import dto.RawFilmDTO;
import entities.FilmReview;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;
import utils.HttpUtils;

public class FilmFacade {
 private static EntityManagerFactory emf =EMF_Creator.createEntityManagerFactory();
    private static FilmFacade instance;
    public FilmFacade() {
    }
    public static FilmFacade getFilmFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FilmFacade();
        }
        return instance;
    }
    
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
    
    public FilmDTO writeFilmRev(FilmDTO fr){
        FilmReview fr1 = new FilmReview(fr);
        System.out.println(fr1.toString());
        System.out.println(fr.toString());
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            System.out.println(fr1);
            em.persist(fr1);
            em.getTransaction().commit();
            return new FilmDTO(fr1);
        }finally {
            em.close();
        }}
    public FilmDTO editFilmRev(FilmDTO fr){
        EntityManager em = emf.createEntityManager();
        FilmReview fr1 = em.find(FilmReview.class, fr.getId());
        try{
            em.getTransaction().begin();
            fr1.setDisplay_title(fr.getDisplay_title());
            fr1.setHeadline(fr.getHeadline());
            fr1.setSummary_short(fr.getSummary_short());
            em.merge(fr1);
            em.getTransaction().commit();
            return new FilmDTO(fr1);
        }finally {
            em.close();
        }}
    public void deleteFilmRev(int nr){
        EntityManager em = emf.createEntityManager();
        FilmReview b = em.find(FilmReview.class,nr);

        try{
            em.getTransaction().begin();
            em.remove(b);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
}
    
    public static void main(String[] args) throws IOException {
        
        FilmFacade facade = new FilmFacade();
        FilmDTO fr = new FilmDTO("Harry Potter","OMG ITS GREAT"," it was so great holy shit idk what to say");
        System.out.println(facade.fetchReviewByTitle("harry potter"));
        System.out.println(facade.fetchReviewByTitle("lebowski"));
        //facade.deleteFilmRev(1);
    }

}
