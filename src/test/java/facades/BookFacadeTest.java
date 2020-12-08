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
            b3=new BookReview("byline3", "title3", "author3", "review3");
            em.createNamedQuery("BookReview.deleteAllRows").executeUpdate();
            em.persist(new BookReview("byline1", "title1", "author1", "review1"));
            em.persist(new BookReview("byline2", "title2", "author2", "review2"));
            em.persist(b3);
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
    public void testGetUserReviewsAlt(){
        List<BookDTO> resultList = facade.getUserReviews("title1");
        //System.out.println(resultList);
        BookDTO dto = resultList.get(0);
        
        assertEquals(dto.getBook_title(), "title1");
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

    //@Test
    public void testFetchBookReviewsIsbn() throws IOException, InterruptedException, ExecutionException {
        String title = "Becoming";
        ReviewsDTO dto = facade.fetchBookReviews(title);
        assertTrue(dto.getIsbm().getIdentifier().length() > 0);
    }

    //@Test
    public void testFetchBookReviewsWidget() throws IOException, InterruptedException, ExecutionException {
        String title = "Becoming";
        ReviewsDTO dto = facade.fetchBookReviews(title);
        assertTrue(dto.getGoodreads().getReviews_widget().contains("Becoming"));
    }
    
     @Test
    public void testAddDataResponse() throws Exception {
        BookDTO b=new BookDTO(new BookReview("bob","bob","bob","bob"));
        assertEquals(b.getBook_author(),facade.writeBookRev(b).getBook_author());
        
         
    }
    
    @Test
    public void testAddworks() throws Exception {
        BookDTO b=new BookDTO(new BookReview("byline","matti","book_author","summary"));
        facade.writeBookRev(b).getBook_author();

        List<BookDTO> resultList = facade.getUserReviews("matti");
        assertTrue(resultList.size()==1);
    }
    
    @Test
    public void testEditWorks() throws Exception {
            BookDTO b=new BookDTO(b3);
            List<BookDTO> resultList = facade.getUserReviews("title3");
            System.out.println(resultList);
            b.setSummary("BIBOB den er god");
            facade.editBookRev(b);

             resultList = facade.getUserReviews("title3");
            System.out.println("Here is the edited list"+resultList);
            assertTrue("BIBOB den er god".equals(resultList.get(0).getSummary()));
    }
    @Test
    public void testDeleteWorks() throws Exception {
        List<BookDTO> resultList = facade.getUserReviews("title3");
        int nr=b3.getId();
        
        facade.deleteBookRev(nr);     
        resultList = facade.getUserReviews("title3");
        assertTrue(resultList.isEmpty());
    }
}
