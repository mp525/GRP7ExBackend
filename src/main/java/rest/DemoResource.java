package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDTO;
import entities.User;
import facades.UserFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade Ufacade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String registerUser(/*String username, String password*/ String givenUser) {
        UserDTO dto = GSON.fromJson(givenUser, UserDTO.class);
        String username = dto.getUsername();
        String password = dto.getPassword();
        EntityManager em = EMF.createEntityManager();
        List<User> users;
        List<String> usernames = new ArrayList();
        String json;
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", User.class);
            users = query.getResultList();
            for (User user : users) {
                usernames.add(user.getUserName());
            }
        } finally {
            em.close();
        }
        
        if (username.isEmpty() || password.isEmpty()) {
            json = GSON.toJson("{\"msg\": \"Both boxes must be filled, try again.\"}");
            return "{\"msg\": \"Both boxes must be filled, try again.\"}";
        } else if (usernames.contains(username)) {
            json = GSON.toJson("{\"msg\": \"Username " + username + " already in use. Try again.\"}");
            return "{\"msg\": \"Username " + username + " already in use. Try again.\"}";
        } else {
            User user = Ufacade.registerUser(username, password);
            json = GSON.toJson("{\"msg\": \"User " + username + " registered\"}");
            if (user != null) {
                return "{\"msg\": \"User " + username + " registered\"}";
            }
        }
        json = GSON.toJson("{\"msg\": \"Action could not be executed. Something went wrong.\"}");
        return "{\"msg\": \"Action could not be executed. Something went wrong.\"}";
    }
}