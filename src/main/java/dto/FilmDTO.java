package dto;

import entities.FilmReview;
import java.util.Date;

/**
 *
 * @author vnord
 */
public class FilmDTO {
    private String display_title;
    private String headline;
    private String summary_short;
    private Date publication_date;
    
    public FilmDTO(String display_title, String headline, String summary_short) {
        this.display_title = display_title;
        this.headline = headline;
        this.summary_short = summary_short;
        this.publication_date = new Date();
    }
public FilmDTO(FilmReview fr) {
        this.display_title = fr.getDisplay_title();
        this.headline = fr.getHeadline();
        this.summary_short = fr.getSummary_short();
        this.publication_date = fr.getPublication_date();
    }
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDisplay_title() {
        return display_title;
    }

    public void setDisplay_title(String display_title) {
        this.display_title = display_title;
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
    public String toString() {
        return "FilmDTO{" + "display_title=" + display_title + ", headline=" + headline + ", summary_short=" + summary_short + ", publication_date=" + publication_date + '}';
    }
    
}
