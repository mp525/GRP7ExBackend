package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.BookDTO;
import dto.FilmDTO;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import facades.FilmFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
 
/**
 * REST Web Service
 *
 * @author lam
 */
@Path("film")
public class FilmResource {
    private final FilmFacade facade = new FilmFacade();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private UriInfo context;
    
    public FilmResource() {
    }

   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDefault() throws IOException, InterruptedException, ExecutionException {
        List<String> list = facade.fetchParallel();
        return GSON.toJson(list);
    }
    
    @GET
    @Path("review/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String getFilmReview(@PathParam("title") String title) throws IOException {
        List<FilmDTO> list = facade.fetchReviewByTitle(title);
        return GSON.toJson(list);
    }
        @GET
    @Path("reviewU/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public String getFilmReviewU(@PathParam("title") String title) throws IOException {
        List<FilmDTO> list = facade.getUserFilmRev(title);
        return GSON.toJson(list);
    }
//    Sending a FilmDTO results in that dtoo being converted to an entity and send to the DB
   @POST
    @Path("add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public FilmDTO addFilmReview(String filmReview) throws IOException {
        FilmDTO fr = GSON.fromJson(filmReview, FilmDTO.class);        
        facade.writeFilmRev(fr);
        return fr;
       
    }
//    Edit film using a FilmDTO with an id
    @PUT
    @Path("edit")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public FilmDTO editFilmReview(String filmrev) throws IOException {
        FilmDTO fr = GSON.fromJson(filmrev, FilmDTO.class);        
        facade.editFilmRev(fr);
        return fr;
       
    }
    //Sending an id to this endpoint results in the filmreview in the db with that id being deleted
    @DELETE
    @Path("delete/{id}")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public FilmDTO deleteFilmReview(@PathParam("id")int id) throws IOException {                
        facade.deleteFilmRev(id);
        return new FilmDTO("has","been","deleted");
       
    }
}
