/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathias
 */
public class RawBookDTO {
    private BookDTO[] results;

    public RawBookDTO(String results) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        this.results = gson.fromJson(results, BookDTO[].class);
    }

    public BookDTO[] getResults() {
        return results;
    }

    public void setResults(BookDTO[] results) {
        this.results = results;
    }

    
    
    
    
}
