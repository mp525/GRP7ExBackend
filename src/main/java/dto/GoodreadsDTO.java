/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author Mathias
 */
public class GoodreadsDTO {
    private String reviews_widget;

    public GoodreadsDTO(String reviews_widget) {
        this.reviews_widget = reviews_widget;
    }

    public String getReviews_widget() {
        return reviews_widget;
    }

    public void setReviews_widget(String reviews_widget) {
        this.reviews_widget = reviews_widget;
    }

    @Override
    public String toString() {
        return "GoodreadsDTO{" + "reviews_widget=" + reviews_widget + '}';
    }
    
    
    
}
