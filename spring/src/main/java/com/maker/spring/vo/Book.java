package com.maker.spring.vo;

import java.io.Serializable;

public class Book implements Serializable {
    private String bid;
    private String title;
    private String author;
    private Double price;

    public Book() {
    }

    public Book(String bid, String title, String author, Double price) {
        this.bid = bid;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
