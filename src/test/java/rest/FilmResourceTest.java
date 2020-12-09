package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.FilmDTO;
import entities.FilmReview;
import entities.Role;
import entities.User;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class FilmResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static FilmReview r1, r2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

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
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        r2 = new FilmReview(new FilmDTO("display_title", "headline", "summary_short"));
        try {
            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

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
            em.persist(r2);
            //System.out.println("Saved test data to database");
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

    @Test
    public void testServerIsUp() {
        given().when().get("/film").then().statusCode(200);
    }

    @Test
    public void testGetFilmReview() throws Exception {
        login("user", "test");
        String[] arr = {"Big Lebowski, the (Movie)"};
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/film/review/lebowski").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("headline", contains(arr));
    }

    @Test
    public void testGetFilmReviews() throws Exception {
        login("user", "test");
        List<FilmDTO> list;
        list = given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/film/review/harry%20potter").then()
                .extract().body().jsonPath().getList("", FilmDTO.class);
        FilmDTO f1 = list.get(0);
        FilmDTO f2 = list.get(1);
        FilmDTO f3 = list.get(2);
        FilmDTO f4 = list.get(3);
        FilmDTO f5 = list.get(4);
        FilmDTO f6 = list.get(5);
        FilmDTO f7 = list.get(6);
        assertThat(list, contains(f1, f2, f3, f4, f5, f6, f7));
    }

    @Test
    public void testPostReview() throws Exception {
        login("user", "test");
        given()
                .contentType("application/json")
                .body(new FilmDTO("display_title", "headline", "summary_short"))
                .header("x-access-token", securityToken)
                .when()
                .post("/film/add")
                .then()
                .body("display_title", equalTo("display_title"))
                .body("headline", equalTo("headline"))
                .body("summary_short", equalTo("summary_short"));

    }

    @Test
    public void testeditReview() throws Exception {
        login("admin", "test");
        FilmReview f3 = new FilmReview("bob", "bent", "børge");
        f3.setId(r2.getId());
        given()
                .contentType("application/json")
                .body(new FilmDTO(f3))
                .header("x-access-token", securityToken)
                .when()
                .put("/film/edit")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("display_title", equalTo("bob"))
                .body("headline", equalTo("bent"))
                .body("summary_short", equalTo("børge"));

    }

    @Test
    public void testdeleteReview() throws Exception {
        login("admin", "test");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .delete("/film/delete/" + r2.getId())
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode());

    }
}
