package com.geraud.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ProductManager {




    private Map<Product, List<Review>> products = new HashMap<>();
//    private Product product;
//    private Review[] reviews = new Review[5];
    private static Map<String , ResourceFormatter> formatters =
        Map.of("en-GB", new ResourceFormatter(Locale.UK),
                "en-US", new ResourceFormatter(Locale.US),
                "fr-FR", new ResourceFormatter(Locale.FRANCE),
                "ru-RU", new ResourceFormatter(new Locale("ru","RU")),
                "zh-CN", new ResourceFormatter(new Locale("zh","CN")));
    private ResourceFormatter formatter;

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale (String languageTag) {
        formatter = formatters.getOrDefault(languageTag , formatters.get("fr-FR"));
    }

    public static Set<String> getSupportedLocales(){
        return formatters.keySet();
    }

    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resourceBundle;
        private DateTimeFormatter dateTimeFormatter;
        private NumberFormat numberFormat;

        private ResourceFormatter (Locale locale){
            this.locale = locale;
            resourceBundle = ResourceBundle.getBundle("com.geraud.data.resources" , locale);
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            numberFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product){
            return MessageFormat.format(resourceBundle.getString("product"),
                    product.getName(),
                    numberFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateTimeFormatter.format(product.getBestBefore()));
        }

        private String formatReview(Review review){
            return MessageFormat.format(resourceBundle.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key) {
            return resourceBundle.getString(key);
        }
    }



    public Product createProduct(int id , String name , BigDecimal price , Rating rating , LocalDate bestBefore){
        Product product =new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id , String name , BigDecimal price , Rating rating ){
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating,String comments){
        return reviewProduct(findProduct(id), rating,comments);
    }

    public Product reviewProduct(Product product , Rating rating , String comments) {

        List<Review> reviews = products.get(product);
        products.remove(product,reviews);
        reviews.add(new Review(rating,comments));
        int sum = 0 ;
        for (Review review: reviews) {
            sum += review.getRating().ordinal();
        }
        product = product.applyRating(Rateable.convert(Math.round((float)sum/reviews.size())));
        products.put(product,reviews);

        return product;
    }

    public void printProductReport(int id){
        printProductReport(findProduct(id));
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProduct(product));
        txt.append('\n');
        if (reviews.isEmpty()){
            txt.append(formatter.getText("no.reviews"));
            txt.append('\n');
        }

        for(Review review : reviews) {

            txt.append(formatter.formatReview(review));
            txt.append('\n');
        }

        txt.append('\n');
        System.out.println(txt);
    }

    public Product findProduct(int id) {
        Product result = null;
        for (Product product: products.keySet()) {
            if (product.getId() == id) {
                result = product;
                break;
            }
        }

        return result;
    }

    public void printProducts (Comparator<Product> sorter) {
        List<Product> productList = new ArrayList<>(products.keySet());
        productList.sort(sorter);
        StringBuilder txt = new StringBuilder();
        for (Product product: productList) {
            txt.append(formatter.formatProduct(product));
            txt.append('\n');
        }
        System.out.print(txt);
    }
}
