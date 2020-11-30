package entities;

import dto.FilmDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery; 
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@NamedQuery(name = "FilmReview.deleteAllRows", query = "DELETE from FilmReview")
public class FilmReview implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String display_title;
    private String headline;
    private String summary_short;
    @Temporal(TemporalType.DATE)
    private Date publication_date;
    
    public FilmReview() {
    }
        
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FilmReview(int id, String display_title, String headline, String summary_short, String publication_date) {
        this.id = id;
        this.display_title = display_title;
        this.headline = headline;
        this.summary_short = summary_short;
        this.publication_date = new Date();
    }
    
    public FilmReview(FilmDTO f) {
        
        this.display_title = f.getDisplay_title();
        this.headline = f.getHeadline();
        this.summary_short = f.getSummary_short();
        this.publication_date = new Date();
    }

    public String getDisplay_title() {
        return display_title;
    }

    public void setDisplay_title(String display_title) {
        this.display_title = display_title;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary_short() {
        return summary_short;
    }

    public void setSummary_short(String summary_short) {
        this.summary_short = summary_short;
    }

    public Date getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(Date publication_date) {
        this.publication_date = publication_date;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.id;
        hash = 59 * hash + Objects.hashCode(this.display_title);
        hash = 59 * hash + Objects.hashCode(this.headline);
        hash = 59 * hash + Objects.hashCode(this.summary_short);
        hash = 59 * hash + Objects.hashCode(this.publication_date);
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
        final FilmReview other = (FilmReview) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.display_title, other.display_title)) {
            return false;
        }
        if (!Objects.equals(this.headline, other.headline)) {
            return false;
        }
        if (!Objects.equals(this.summary_short, other.summary_short)) {
            return false;
        }
        if (!Objects.equals(this.publication_date, other.publication_date)) {
            return false;
        }
        return true;
    }
    
   
    
    
    

   
}
