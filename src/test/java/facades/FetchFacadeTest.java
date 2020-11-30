package facades;

import dto.FilmDTO;
import entities.FilmReview;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.EMF_Creator;

/**
 *
 * @author vnord
 */
public class FetchFacadeTest {
    
    private static EntityManagerFactory emf;
    private static FilmFacade facade;
      private FilmReview f1;
    private FilmReview f2;
    private FilmReview f3;
    public FetchFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = new FilmFacade();
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    //Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            f1=new FilmReview("Bob","binno","bulkabilo");
             f2=new FilmReview("Bob2","binno2","bulkabilo2"); 
             f3=new FilmReview("Bob3","binno3","bulkabilo3");
             
            em.getTransaction().begin();
            em.createNamedQuery("FilmReview.deleteAllRows").executeUpdate();            
            em.persist(f1);
            em.persist(f2);
            em.persist(f3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of fetchReviewByTitle method, of class FetchFacade.
     * Test of a single search result.
     */
    @Test
    public void testFetchReviewByTitle() throws Exception {
        String title = "lebowski";
        String expResult = "The Big Lebowski";
        List<FilmDTO> resultList = facade.fetchReviewByTitle(title);
        assertEquals(expResult, resultList.get(0).getDisplay_title());
    }
    
    /**
     * Test of fetchReviewByTitle method, of class FetchFacade.
     * Test of multiple search results. Does every item have a summary/review.
     */
    @Test
    public void testFetchReviewsByTitle() throws Exception {
        String title = "harry potter";
        List<FilmDTO> resultList = facade.fetchReviewByTitle(title);
        assertThat(resultList, everyItem(hasProperty("summary_short")));
    }
    
    /**
     * Test of fetchReviewByTitle method, of class FetchFacade.
     * Test of multiple search results. Does every title have the search words.
     */
//    @Test
//    public void testFetchReviewsByTitle2() throws Exception {
//        String title = "Harry Potter";
//        List<FilmDTO> resultList = facade.fetchReviewByTitle(title);
//        assertThat(resultList, hasItems(
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Sorcerer\\u0027s Stone")),
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Chamber of Secrets")),
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Prisoner of Azkaban")),
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Order of the Phoenix")),
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Half-Blood Prince")),
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Deathly Hallows: Part 1")),
//                Matchers.<FilmDTO>hasProperty("display_title", is("Harry Potter and the Deathly Hallows: Part 2"))
//        ));
//    }
    
    @Test
    public void testAdd() throws Exception {
    facade.writeFilmRev(new FilmDTO(f1));
    
    }
}
