package dto;

/**
 *
 * @author vnord
 */
public class FilmDTO {
    private String display_title;
    private String headline;
    private String summary_short;
    private String publication_date;
    private String date_updated;

    public FilmDTO(String display_title, String headline, String summary_short, String publication_date, String date_updated) {
        this.display_title = display_title;
        this.headline = headline;
        this.summary_short = summary_short;
        this.publication_date = publication_date;
        this.date_updated = date_updated;
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

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    @Override
    public String toString() {
        return "FilmDTO{" + "display_title=" + display_title + ", headline=" + headline + ", summary_short=" + summary_short + ", publication_date=" + publication_date + ", date_updated=" + date_updated + '}';
    }
    
}
