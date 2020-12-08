package facades;

import dto.UserDTO;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    public List<UserDTO> getAllUsers(){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            List<UserDTO> dtos = new ArrayList();
            for (User user : users) {
                dtos.add(new UserDTO(user));
            }
            return dtos;
        } finally {
            em.close();
        }
    }
    
    public User registerUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        User user;
        Role userRole = new Role("user");
        try {
            em.getTransaction().begin();
            user = new User(username, password);
            user.addRole(userRole);
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }
    
    public static void main(String[] args) {
        UserFacade uf = new UserFacade();
        uf.registerUser("hei", "hell");
        //instance.registerUser("fuck", "this");
    }

}
