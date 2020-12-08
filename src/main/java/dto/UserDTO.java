package dto;

import entities.User;
import java.util.Objects;

/**
 *
 * @author vnord
 */
public class UserDTO {
    private String username;
    private String password;

    public UserDTO() {
    }
    
    public UserDTO(User user) {
        this.username = user.getUserName();
        this.password = user.getUserPass();
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.username);
        hash = 47 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDTO other = (UserDTO) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", password=" + password + '}';
    }
    
    
}
