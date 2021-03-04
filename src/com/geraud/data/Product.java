package com.geraud.data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import static com.geraud.data.Rating.NOT_RATED;

public abstract class Product implements Rateable<Product>{
    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;
    private static final BigDecimal DISCOUNT_RATE =BigDecimal.valueOf(0.1);

    Product() {
        this(0, "no name" , BigDecimal.ZERO);
    }

    Product(int id, String name, BigDecimal price) {
        this(id,name,price,NOT_RATED);
    }

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscount(){
        return price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Rating getRating() {
        return rating;
    }

    public LocalDate getBestBefore() {
        return LocalDate.now();
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount = " + getDiscount() +
                ", rating=" + rating.getStars() +
                "best before " + getBestBefore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ( o instanceof Product) {
            Product product = (Product) o;
            return id == product.id && Objects.equals(name, product.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
