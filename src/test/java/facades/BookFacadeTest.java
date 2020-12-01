/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.BookDTO;
import dto.RawBookDTO;
import dto.ReviewsDTO;
import entities.BookReview;
import entities.FilmReview;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

/**
 *
 * @author Mathias
 */
public class BookFacadeTest {

    private static EntityManagerFactory emf;
    private static BookFacade facade = new BookFacade();

    ;
     private BookReview b1;
    private BookReview b2;
    private BookReview b3;
    public BookFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = BookFacade.getBookFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() { 
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("BookReview.deleteAllRows").executeUpdate();
            em.persist(new BookReview("byline1", "title1", "author1", "review1"));
            em.persist(new BookReview("byline2", "title2", "author2", "review2"));
            b3=new BookReview("byline3", "title3", "author3", "review3");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testGetUserReviews(){
        List<BookDTO> resultList = facade.getUserReviews("title");
        assertTrue(!resultList.isEmpty());
    }

    @Test
    public void testFetchBookReviewsOld() throws IOException, IOException {
        String title = "Becoming";
        String expected = "Michelle Obama";
        List<BookDTO> dtoList = facade.fetchBookReviewsOld(title);
        assertEquals(expected, dtoList.get(0).getBook_author());
    }

    @Test
    public void testFetchBookReviewsBooks() throws IOException, InterruptedException, ExecutionException {
        String title = "Becoming";
        ReviewsDTO dto = facade.fetchBookReviews(title);
        assertTrue(!dto.getBookDTOs().isEmpty());
    }

    @Test
    public void testFetchBookReviewsIsbn() throws IOException, InterruptedException, ExecutionException {
        String title = "Becoming";
        ReviewsDTO dto = facade.fetchBookReviews(title);
        assertTrue(dto.getIsbm().getIdentifier().length() > 0);
    }

    @Test
    public void testFetchBookReviewsWidget() throws IOException, InterruptedException, ExecutionException {
        String title = "Becoming";
        ReviewsDTO dto = facade.fetchBookReviews(title);
        assertTrue(dto.getGoodreads().getReviews_widget().contains("Becoming"));
    }
    
     @Test
    public void testAdd() throws Exception {
        List<BookDTO> resultList = facade.getUserReviews("title");
        BookDTO b=new BookDTO(new BookReview("bob","bob","bob","bob"));
        facade.writeBookRev(b);
        
        
         System.out.println(resultList);
    }
}
