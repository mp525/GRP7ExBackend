/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.BookDTO;
import dto.FilmDTO;
import entities.BookReview;
import entities.Role;
import entities.User;
import facades.BookFacade;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import static org.hamcrest.Matchers.is;
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
public class BooksResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    //private BookDTO dto;
    private BookReview bookReview;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final BookFacade facade = new BookFacade();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void tearDownClass() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        //BookDTO bookDTO = new BookDTO("RevAuthor","title","author","review");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.createQuery("delete from BookReview").executeUpdate();

            BookDTO bookDTO = new BookDTO("RevAuthor", "title", "author", "review");
            bookReview = new BookReview(bookDTO);

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.persist(bookReview);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    public void serverIsRunning() {
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void testGetReviewsOld() {
        login("user", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/books/reviewsOld/Becoming").then()
                .statusCode(200)
                .body("book_author", Matchers.contains("Michelle Obama"));
    }

    @Test
    public void testGetReviewsBooks() {

        login("user", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/books/reviews/Becoming")
                .then()
                .statusCode(200)
                .body(Matchers.containsString("Becoming"));
        //Hvorfor er dette det tætteste jeg kan komme på at teste bookDTOs?!        
    }

//    @Test
    public void testGetReviewsBooksAdmin() {

        login("admin", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/books/reviewsA/Becoming")
                .then()
                .statusCode(200)
                .body(Matchers.containsString("Becoming"));

    }

    @Test
    public void testEditReviews() {
        BookDTO dto = new BookDTO(bookReview);
        dto.setSummary("edited review");

        login("admin", "test");
        given()
                .contentType("application/json")
                .body(dto)
                .header("x-access-token", securityToken)
                .when()
                .put("/books/edit")
                .then()
                .statusCode(200)
                .body("summary", equalTo("edited review"));

    }

    @Test
    public void testDeleteReviews() {
        login("admin", "test");
        given()
                .pathParam("id", bookReview.getId())
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .delete("/books/delete/{id}")
                .then()
                .statusCode(200)
                .body("byline", equalTo("It"))
                .body("book_title", equalTo("has"))
                .body("book_author", equalTo("been"))
                .body("summary", equalTo("deleted"));

    }

    @Test
    public void testPostReview() throws Exception {
        login("user", "test");
        given()
                .contentType("application/json")
                .body(new BookDTO("byline", "book_title", "book_author", "summary"))
                .header("x-access-token", securityToken)
                .when()
                .post("/books/add")
                .then()
                .body("book_title", equalTo("book_title"))
                .body("book_author", equalTo("book_author"))
                .body("byline", equalTo("byline"))
                .body("summary", equalTo("summary"));

    }

}
