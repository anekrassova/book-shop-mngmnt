package com.example.bookshop;

import java.util.ArrayList;

public class Sale {
    private int sale_id;
    private double sale_total = 0;

    public Sale(int sale_id) {
        this.sale_id = sale_id;
    }
    public Sale(){}
    public Sale(int sale_id, double sale_total) {
        this.sale_id = sale_id;
        this.sale_total = sale_total;
    }
    ArrayList<BookData> orderedBooks = new ArrayList<>();

    public void addBook(BookData book) {
        orderedBooks.add(book);
    }
    public int getSale_id() {
        return sale_id;
    }
    public double getSale_total() {
        return sale_total;
    }
    public void setSale_total(double sale_total) {
        this.sale_total = sale_total;
    }

    public void sale_total_reset(){
        sale_total = 0;
    }
    public void orderedBooks_reset(){
        orderedBooks.clear();
    }
}
