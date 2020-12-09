package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dto.UserDTO;
import entities.User;
import errorhandling.NotFoundException;
import facades.UserFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import static security.LoginEndpoint.TOKEN_EXPIRE_TIME;
import security.SharedSecret;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade Ufacade = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    @RolesAllowed("admin")
    public String allUsers() {
        List<UserDTO> users = Ufacade.getAllUsers();
        return GSON.toJson(users);
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{username}")
    @RolesAllowed("admin")
    public String deleteUser(@PathParam("username") String username) throws NotFoundException {
        User user = Ufacade.deleteUser(username);
        if (user != null) {
            return GSON.toJson(user);
        }
        throw new NotFoundException("Username not found. Try again.");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(String givenUser) throws AuthenticationException, JOSEException {
        UserDTO dto = GSON.fromJson(givenUser, UserDTO.class);
        String username = dto.getUsername();
        String password = dto.getPassword();
        EntityManager em = EMF.createEntityManager();
        List<User> users;
        List<String> usernames = new ArrayList();
        //String json;
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
            //json = GSON.toJson("{\"msg\": \"Both boxes must be filled, try again.\"}");
            //return "{\"msg\": \"Both boxes must be filled, try again.\"}";
        } else if (usernames.contains(username)) {
            //json = GSON.toJson("{\"msg\": \"Username " + username + " already in use. Try again.\"}");
            //return "{\"msg\": \"Username " + username + " already in use. Try again.\"}";
        } else {
            User user = Ufacade.registerUser(username, password);
            //json = GSON.toJson("{\"msg\": \"User " + username + " registered\"}");
            try {
                return quickLogin(username, password);

            } catch (JOSEException | AuthenticationException ex) {
                if (ex instanceof AuthenticationException) {
                    throw (AuthenticationException) ex;
                }
            }

            if (user != null) {
                //return "{\"msg\": \"User " + username + " registered\"}";
            }
        }
        //json = GSON.toJson("{\"msg\": \"Action could not be executed. Something went wrong.\"}");
        //return "{\"msg\": \"Action could not be executed. Something went wrong.\"}";
        throw new AuthenticationException("Invalid username or password! Please try again");
    }

    private Response quickLogin(String username, String password) throws AuthenticationException, JOSEException {
        User verifiedUser = Ufacade.getVeryfiedUser(username, password);
        String token = createToken(username, verifiedUser.getRolesAsStrings());
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("username", username);
        responseJson.addProperty("token", token);
        return Response.ok(new Gson().toJson(responseJson)).build();
    }

    private String createToken(String userName, List<String> roles) throws JOSEException {

        StringBuilder res = new StringBuilder();
        for (String string : roles) {
            res.append(string);
            res.append(",");
        }
        String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";
        String issuer = "semesterstartcode-dat3";

        JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
        Date date = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .claim("username", userName)
                .claim("roles", rolesAsString)
                .claim("issuer", issuer)
                .issueTime(date)
                .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();

    }
}
