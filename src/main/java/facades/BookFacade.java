package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dto.BookDTO;
import dto.GoodreadsDTO;
import dto.IdentityIsbmDTO;
import dto.ItemsDTO;
import dto.RawBookDTO;
import dto.ReviewsDTO;
import entities.BookReview;
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
import javax.persistence.TypedQuery;
import utils.EMF_Creator;
import utils.HttpUtils;

public class BookFacade {

    private static BookFacade instance;
    private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

    public BookFacade() {
    }

    public static BookFacade getBookFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new BookFacade();
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

    public ReviewsDTO fetchBookReviews(String title) throws IOException, InterruptedException, ExecutionException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<BookDTO> books = new ArrayList();
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> futures = new ArrayList<>();

        String titleF = title.replace(" ", "%20");
        String[] urls = {"https://api.nytimes.com/svc/books/v3/reviews.json?api-key=5tN35qLGRRkvgYSCFj7wKdwhDNb5PMOF&title=" + titleF,
            "https://www.googleapis.com/books/v1/volumes/?key=AIzaSyDmYIEO0_nFDW06PK_QZUGR4bVLmliaq60&q=" + titleF,
            "https://www.goodreads.com/book/title.json?key=Z5W4tEAU4SuwDUOzz6YNQ&title=" + titleF
        };

        for (String url : urls) {
            Callable<String> urlTask = new Default(url);
            Future future = executor.submit(urlTask);
            futures.add(future);
        }

        IdentityIsbmDTO isbmDTO = null;
        GoodreadsDTO goodreads = null;

        for (Future<String> fut : futures) {
            makeBookDTOs(fut, gson, books);
            goodreads = makeGoodreadsDTO(fut, goodreads, gson);
            isbmDTO = getIsbm(fut, gson, isbmDTO);
        }

        List<BookDTO> userReviews = getUserReviews(title);
        if (!(userReviews.isEmpty())) {
            for (BookDTO userReview : userReviews) {
                books.add(userReview);
            }
        }

        ReviewsDTO reviews = new ReviewsDTO(books, isbmDTO, goodreads);
        return reviews;
    }

    private IdentityIsbmDTO getIsbm(Future<String> fut, Gson gson, IdentityIsbmDTO isbmDTO) throws JsonSyntaxException, InterruptedException, ExecutionException {
        if (fut.get().contains("items")) {
            ItemsDTO items = gson.fromJson(fut.get(), ItemsDTO.class);

            IdentityIsbmDTO[] identifiers = items.getItems()[0].getVolumeInfo().getIndustryIdentifiers();
            if (identifiers[0].toString().length() < identifiers[1].toString().length()) {
                isbmDTO = identifiers[0];
            } else {
                isbmDTO = identifiers[1];
            }

        }
        return isbmDTO;
    }

    private GoodreadsDTO makeGoodreadsDTO(Future<String> fut, GoodreadsDTO goodreads, Gson gson) throws InterruptedException, ExecutionException, JsonSyntaxException {
        if (fut.get().contains("reviews_widget")) {
            goodreads = gson.fromJson(fut.get(), GoodreadsDTO.class);
        }
        return goodreads;
    }

    private void makeBookDTOs(Future<String> fut, Gson gson, List<BookDTO> books) throws InterruptedException, JsonSyntaxException, ExecutionException {
        if (fut.get().contains("The New York Times Company.")) {
            RawBookDTO rawBook = gson.fromJson(fut.get(), RawBookDTO.class);
            for (BookDTO result : rawBook.getResults()) {
                System.out.println(result.getBook_author());
                books.add(result);
            }
        }
    }

    public List<BookDTO> getUserReviews(String title) {
        List<BookDTO> list = new ArrayList();
        EntityManager em = emf.createEntityManager();

        TypedQuery<BookReview> query = em.createQuery("SELECT b FROM BookReview b WHERE b.title LIKE :title", BookReview.class);
        query.setParameter("title", "%" + title + "%");
        List<BookReview> reviews = query.getResultList();

        if (!(reviews.isEmpty())) {
            for (BookReview review : reviews) {
                list.add(new BookDTO(review));
            }
        }

        return list;
    }

//    public IdentityIsbmDTO getBookIsbn(String title) throws IOException {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String titleF = title.replace(" ", "%20");
//
//        String url = "https://www.googleapis.com/books/v1/volumes/?key=AIzaSyDmYIEO0_nFDW06PK_QZUGR4bVLmliaq60&q=" + titleF;
//        String raw = HttpUtils.fetchData(url);
//        ItemsDTO items = gson.fromJson(raw, ItemsDTO.class);
//        IdentityIsbmDTO[] identifiers = items.getItems()[0].getVolumeInfo().getIndustryIdentifiers();
//
//        IdentityIsbmDTO isbmDTO = identifiers[0];
//        return isbmDTO;
//    }
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

    public List<BookDTO> fetchBookReviewsOld(String title) throws IOException {
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

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        BookFacade facade = new BookFacade();
        EntityManagerFactory emf2 = EMF_Creator.createEntityManagerFactory();
         EntityManager em = emf2.createEntityManager();
        try {
            em.getTransaction().begin();
            //em.createNamedQuery("BookReview.deleteAllRows").executeUpdate();
            //em.persist(new BookReview("byline1", "title1", "author1", "review1"));
            em.persist(new BookReview("Mathias P", "Becoming", "Michelle Obama", "This book is probably the best book ever written in the entire universe."));

            em.getTransaction().commit();
        } finally {
            em.close();
        }

        //System.out.println(facade.fetchBookReviews("1Q84").toString());
        //System.out.println(facade.getBookIsbn("Quilting For Dummies"));
    }

}
