/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dto.BookDTO;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author matti
 */
@Entity
@NamedQuery(name = "BookReview.deleteAllRows", query = "DELETE from BookReview")
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    private Date publication;
    private String byline;
    private String title;
    private String author;
    private String summary;

    public BookReview() {
    }

    public BookReview(String byline, String title, String author, String summary) {
        this.publication = new Date();
        this.byline = byline;
        this.title = title;
        this.author = author;
        this.summary = summary;
    }
public BookReview(BookDTO d) {
        this.publication = new Date();
        this.byline = d.getByline();
        this.title = d.getBook_title();
        this.author = d.getBook_author();
        this.summary = d.getSummary();
    }
    public Date getPublication() {
        return publication;
    }

    public void setPublication(Date publication) {
        this.publication = publication;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.publication);
        hash = 97 * hash + Objects.hashCode(this.byline);
        hash = 97 * hash + Objects.hashCode(this.title);
        hash = 97 * hash + Objects.hashCode(this.author);
        hash = 97 * hash + Objects.hashCode(this.summary);
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
        final BookReview other = (BookReview) obj;
        if (!Objects.equals(this.publication, other.publication)) {
            return false;
        }
        if (!Objects.equals(this.byline, other.byline)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.summary, other.summary)) {
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BookReview{" + "publication=" + publication + ", byline=" + byline + ", title=" + title + ", author=" + author + ", summary=" + summary + '}';
    }
    
}
