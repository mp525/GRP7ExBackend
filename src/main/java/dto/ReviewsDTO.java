/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author Mathias
 */
public class ReviewsDTO {
    private List<BookDTO> bookDTOs;
    private IdentityIsbmDTO isbm;
    private GoodreadsDTO goodreads;

    public ReviewsDTO(List<BookDTO> bookDTOs, IdentityIsbmDTO isbm, GoodreadsDTO goodreads) {
        this.bookDTOs = bookDTOs;
        this.isbm = isbm;
        this.goodreads = goodreads;
    }

    public List<BookDTO> getBookDTOs() {
        return bookDTOs;
    }

    public void setBookDTOs(List<BookDTO> bookDTOs) {
        this.bookDTOs = bookDTOs;
    }

    public IdentityIsbmDTO getIsbm() {
        return isbm;
    }

    public void setIsbm(IdentityIsbmDTO isbm) {
        this.isbm = isbm;
    }

    public GoodreadsDTO getGoodreads() {
        return goodreads;
    }

    public void setGoodreads(GoodreadsDTO goodreads) {
        this.goodreads = goodreads;
    }

    @Override
    public String toString() {
        return "ReviewsDTO{" + "bookDTOs=" + bookDTOs + ", isbm=" + isbm + ", goodreads=" + goodreads + '}';
    }
    
    
    
}
