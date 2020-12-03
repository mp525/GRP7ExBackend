/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.BookDTO;
import dto.ReviewsDTO;
import facades.BookFacade;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author Mathias
 */
@Path("books")
public class BooksResource {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final BookFacade facade = new BookFacade();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;
    /**
     * Creates a new instance of BooksResource
     */
    public BooksResource() {
    }

    /**
     * Retrieves representation of an instance of rest.BooksResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    @Path("reviewsOld/{title}")
    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReviewsOld(@PathParam("title") String title) throws IOException {
        List<BookDTO> list = facade.fetchBookReviewsOld(title);
        return GSON.toJson(list);
    }

    @Path("reviews/{title}")
    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReviews(@PathParam("title") String title) throws IOException, InterruptedException, ExecutionException {
        ReviewsDTO dto = facade.fetchBookReviews(title);
        return GSON.toJson(dto);
    }
    
    @Path("reviewsA/{title}")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public String getReviewsA(@PathParam("title") String title) throws IOException, InterruptedException, ExecutionException {
        ReviewsDTO dto = facade.fetchBookReviews(title);
        return GSON.toJson(dto);
    }
    
    /**
     * PUT method for updating or creating an instance of BooksResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    //    Sending a FilmDTO results in that dtoo being converted to an entity and send to the DB
    @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO addBookReview(String bookReview) throws IOException {
        BookDTO fr = GSON.fromJson(bookReview, BookDTO.class);        
        facade.writeBookRev(fr);
        return fr;
       
    }
    //    Edit book using a BookDTO with an id
    @PUT
    @Path("edit")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO editBookReview(String bookReview) throws IOException {
        BookDTO fr = GSON.fromJson(bookReview, BookDTO.class);        
        facade.editBookRev(fr);
        return fr;
       
    }
    //Sending an id to this endpoint results in the bookreview in the db with that id being deleted
    @DELETE
    @Path("delete/{id}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public BookDTO deleteBookReview(@PathParam("id")int id) throws IOException {
                
        facade.deleteBookRev(id);
        return new BookDTO("It","has","been","deleted");
       
    }
}
