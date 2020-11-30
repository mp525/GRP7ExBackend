package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
   
}
