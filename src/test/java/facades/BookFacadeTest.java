/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.BookDTO;
import dto.RawBookDTO;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Mathias
 */
public class BookFacadeTest {
    private static EntityManagerFactory emf;
    private static BookFacade facade = new BookFacade();;
    
    public BookFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testFetchBookReviewsOld() throws IOException, IOException{
        
        String title = "Becoming";
        String expected = "Michelle Obama";
        List<BookDTO> dtoList = facade.fetchBookReviewsOld(title);
        assertEquals(expected, dtoList.get(0).getBook_author());
    }
    
}
