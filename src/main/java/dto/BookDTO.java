/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.BookReview;
import java.util.Date;

/**
 *
 * @author Mathias
 */
public class BookDTO {

    private int id;
    private String url;
    private String publication_dt;
    private String byline;
    private String book_title;
    private String book_author;
    private String summary;

    public BookDTO(String url, String publication_dt, String byline, String book_title, String book_author, String summary) {
        this.url = url;
        this.publication_dt = publication_dt;
        this.byline = byline;
        this.book_title = book_title;
        this.book_author = book_author;
        this.summary = summary;
    }

    public BookDTO(String byline, String book_title, String book_author, String summary) {
        this.url = "none";
        this.publication_dt = new Date().toString();
        this.byline = byline;
        this.book_title = book_title;
        this.book_author = book_author;
        this.summary = summary;
    }

    public BookDTO(BookReview review) {
        this.id = review.getId();
        this.url = "none";
        this.publication_dt = review.getPublication().toString();
        this.byline = review.getByline();
        this.book_title = review.getTitle();
        this.book_author = review.getAuthor();
        this.summary = review.getSummary();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublication_dt() {
        return publication_dt;
    }

    public void setPublication_dt(String publication_dt) {
        this.publication_dt = publication_dt;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "BookDTO{" + "url=" + url + ", publication_dt=" + publication_dt + ", byline=" + byline + ", book_title=" + book_title + ", book_author=" + book_author + ", summary=" + summary + '}';
    }

}
