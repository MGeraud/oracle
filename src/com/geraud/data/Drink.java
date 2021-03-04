package com.geraud.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class Drink extends Product{

    Drink(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }

    @Override
    public BigDecimal getDiscount() {
        return (LocalTime.now().isAfter(LocalTime.of(17,30))
                && LocalTime.now().isBefore(LocalTime.of(18,30))
                ? super.getDiscount() : BigDecimal.ZERO );

    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Drink(this.getId(),this.getName(),getPrice(),newRating);
    }
}
