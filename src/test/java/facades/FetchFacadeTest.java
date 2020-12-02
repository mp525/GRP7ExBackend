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
        facade = FilmFacade.getFilmFacade(emf);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    //Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            f1=new FilmReview("oklahoma","binno","bulkabilo");
             f2=new FilmReview("Bob2","binno2","bulkabilo2"); 
             f3=new FilmReview("Bob3","binno3","bulkabilo3");
             
            em.getTransaction().begin();
            em.createNamedQuery("FilmReview.deleteAllRows").executeUpdate();            
            em.persist(f1);
            em.persist(f2);
            //em.persist(f3);
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
    
    @Test
    public void testAddDataResponse() throws Exception {
    FilmDTO f=facade.writeFilmRev(new FilmDTO(f1));
    assertEquals(f1.getSummary_short(),f.getSummary_short());
    }
    
    //tester om add kan adde et film review
    @Test
    public void testAddworks() throws Exception {
        FilmDTO b=new FilmDTO(new FilmReview("matti","book_author","summary"));
        facade.writeFilmRev(b);
        List<FilmDTO> resultList = facade.getUserFilmRev("matti");
        assertTrue(resultList.size()==1);
    }
    
    //Tester om edit kan edit et filmreview
    @Test
    public void testEditWorks() throws Exception {
            FilmDTO b=new FilmDTO(f1);
            List<FilmDTO> resultList = facade.getUserFilmRev("oklahoma");
            System.out.println("before edit"+resultList);
            b.setSummary_short("BIBOB den er god");
            facade.editFilmRev(b);
            resultList = facade.getUserFilmRev("oklahoma");
            assertTrue("BIBOB den er god".equals(resultList.get(0).getSummary_short()));
    }
    //Tester om delete kan delete et film review
    @Test
    public void testDeleteWorks() throws Exception {
        List<FilmDTO> resultList = facade.getUserFilmRev("oklahoma");
        int nr=f1.getId();
        facade.deleteFilmRev(nr);     
        resultList = facade.getUserFilmRev("oklahoma");
        assertTrue(resultList.isEmpty());
    }
}
