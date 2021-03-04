package com.geraud.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductManager {

    private Product product;
    private Review[] reviews = new Review[5];
    private Locale locale;
    private ResourceBundle resourceBundle;
    private DateTimeFormatter dateTimeFormatter;
    private NumberFormat numberFormat;

    public ProductManager(Locale locale) {
        this.locale = locale;
        resourceBundle = ResourceBundle.getBundle("com.geraud.data.resources" , locale);
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
        numberFormat = NumberFormat.getCurrencyInstance(locale);
    }

    public Product createProduct(int id , String name , BigDecimal price , Rating rating , LocalDate bestBefore){
        product =new Food(id, name, price, rating, bestBefore);
        return product;
    }

    public Product createProduct(int id , String name , BigDecimal price , Rating rating ){
        product = new Drink(id, name, price, rating);
        return product;
    }

    public Product reviewProduct(Product product , Rating rating , String comments) {
        if (reviews[reviews.length-1] != null){
            reviews = Arrays.copyOf(reviews,reviews.length+5);
        }
        int sum = 0 , i = 0;
        boolean reviewed = false;
        while (i < reviews.length && !reviewed){
            if (reviews[i] == null) {
                reviews[i] = new Review(rating, comments);
                reviewed = true;
            }
            sum += reviews[i].getRating().ordinal();
            i++;
        }
        this.product = product.applyRating(Rateable.convert(Math.round((float)sum/i)));
        return this.product;
    }

    public void printProductReport() {
        StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resourceBundle.getString("product"),
                product.getName(),
                numberFormat.format(product.getPrice()),
                product.getRating().getStars(),
                dateTimeFormatter.format(product.getBestBefore())));
        txt.append('\n');
        for(Review review : reviews) {
            if (review == null) {
                break;
            }
            txt.append(MessageFormat.format(resourceBundle.getString("review"),
                        review.getRating().getStars(),
                        review.getComments()));
            txt.append('\n');
        }
        if (reviews[0] == null) {
            txt.append(resourceBundle.getString("no.reviews"));
        }
        txt.append('\n');
        System.out.println(txt);
    }
}
