package com.geraud.data;

@FunctionalInterface //s'assure qu 'une seule methode abstraite soit dans l'interface
public interface Rateable <T> {
    Rating DEFAULT_RATING = Rating.NOT_RATED;

    T applyRating(Rating rating);

    public default T applyRating(int stars){
        return applyRating(convert(stars));
    };

    default Rating getRating() {
        return DEFAULT_RATING;
    }
    public static Rating convert (int stars) {
        return (stars >= 0 && stars <= 5) ? Rating.values()[stars] : DEFAULT_RATING;
    }


}
