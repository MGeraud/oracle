package com.geraud;

import com.geraud.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager("ru-RU");

//        pm.createProduct(101,"Tea", BigDecimal.valueOf(1.99),Rating.NOT_RATED);

        pm.parseProduct("F,101,Tea,1.99,0,2019-09-19");
        pm.printProductReport(101);
        pm.parseReview("101,4,Nice cup of tea");
        pm.parseReview("101,3,Nice hot cup of Tea!");
        pm.parseReview("101,4,Good Tea!");
        pm.parseReview("101,4,Perfect Tea!");
        pm.parseReview("101,2,Quite cold Tea!");
        pm.parseReview("101,4,Just add some lemon");
        pm.parseReview("101,4,fine Tea!");

        pm.printProductReport(101);

//        pm.changeLocale("fr-FR");

        pm.parseProduct("D,102,Coffee,2.99,0,2019-03-03");
//        pm.reviewProduct(102, Rating.ONE_STAR,"Need milk!");
//        pm.reviewProduct(102, Rating.FOUR_STAR,"Good Coffee!");
//        pm.reviewProduct(102, Rating.FOUR_STAR,"Perfect Coffee!");
//        pm.reviewProduct(102, Rating.TWO_STAR,"Quite cold Coffee!");
        pm.printProductReport(102);
//
//        pm.createProduct(103,"Cake", BigDecimal.valueOf(1.99),Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        pm.reviewProduct(103, Rating.FIVE_STAR,"Nice hot cup of p3!");
//        pm.reviewProduct(103, Rating.FOUR_STAR,"Good p3!");
//        pm.reviewProduct(103, Rating.FOUR_STAR,"Perfect p3!");
//        pm.reviewProduct(103, Rating.FIVE_STAR,"Quite cold p3!");
////        pm.printProductReport(103);
//
////        pm.changeLocale("en-US");
//
//        pm.createProduct(104 , "Cookies" , BigDecimal.valueOf(3.99) , Rating.TWO_STAR , LocalDate.now());
//        pm.reviewProduct(104, Rating.NOT_RATED,"Nice hot cup of p4!");
//        pm.reviewProduct(104, Rating.ONE_STAR,"Good p4!");
//        pm.reviewProduct(104, Rating.FOUR_STAR,"Perfect p4!");
//        pm.reviewProduct(104, Rating.TWO_STAR,"Quite cold p4!");
////        pm.printProductReport(104);
//
//        pm.createProduct(105, "Chocolate" , BigDecimal.valueOf(2.99),Rating.FIVE_STAR);
//        pm.reviewProduct(105, Rating.TWO_STAR,"Nice hot cup of Tea!");
//        pm.reviewProduct(105, Rating.THREE_STAR,"Good Tea!");
//        pm.reviewProduct(105, Rating.TWO_STAR,"Perfect Tea!");
//        pm.reviewProduct(105, Rating.TWO_STAR,"Quite cold Tea!");
////        pm.printProductReport(105);
//
//        pm.createProduct(106, "Chocolate" , BigDecimal.valueOf(2.99),Rating.FIVE_STAR , LocalDate.now().plusDays(2));
//        pm.reviewProduct(106, Rating.FIVE_STAR,"Nice hot cup of p6!");
//        pm.reviewProduct(106, Rating.FOUR_STAR,"Good p6!");
//        pm.reviewProduct(106, Rating.FIVE_STAR,"Perfect p6!");
//        pm.reviewProduct(106, Rating.FIVE_STAR,"Quite cold p6!");
//        pm.printProductReport(106);
//
//
//        Comparator<Product> ratingComparator = Comparator.comparing(Product::getRating).reversed();
//        Comparator<Product> priceComparator = Comparator.comparing(Product::getPrice);
//
////        pm.printProducts(ratingComparator.thenComparing(priceComparator));
//        pm.printProducts(p -> p.getPrice().floatValue() < 2 , ratingComparator.thenComparing(priceComparator));
//        pm.getDiscounts().forEach(
//                (rating, discount) -> System.out.println(rating+"\t" + discount)
//        );
    }
}
