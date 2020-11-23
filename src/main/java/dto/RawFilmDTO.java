package dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author vnord
 */
public class RawFilmDTO {
    private FilmDTO[] results;

    public RawFilmDTO(String results) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        this.results = gson.fromJson(results, FilmDTO[].class);
    }
    
    public FilmDTO[] getResults() {
        return results;
    }
    
    public void setResults(FilmDTO[] results) {
        this.results = results;
    }
    
}
