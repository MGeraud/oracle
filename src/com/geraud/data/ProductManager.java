package com.geraud.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {


    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private ResourceBundle config = ResourceBundle.getBundle("com.geraud.data.config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat producFormat = new MessageFormat(config.getString("product.data.format"));

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
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException e){
            logger.log(Level.INFO,  e.getMessage());
            return null;
        }
    }

    public Product reviewProduct(Product product , Rating rating , String comments) {

        List<Review> reviews = products.get(product);
        products.remove(product,reviews);
        reviews.add(new Review(rating,comments));

        product = product.applyRating(
                Rateable.convert(
                        (int) Math.round(
                                reviews.stream()
                                        .mapToInt(r -> r.getRating().ordinal())
                                        .average()
                                        .orElse(0))));
        products.put(product,reviews);

        return product;
    }

    public void printProductReport(int id){
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO,  e.getMessage());
        }
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        Collections.sort(reviews);
        StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProduct(product));
        txt.append('\n');

        if (reviews.isEmpty()){
            txt.append(formatter.getText("no.reviews"));
        } else {
            txt.append(reviews.stream()
                    .map(r -> formatter.formatReview(r) + '\n')
                    .collect(Collectors.joining()));
        }
        System.out.println(txt);
    }

    public Product findProduct(int id) throws ProductManagerException {

        return  products.keySet().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));
    }

    public void printProducts (Predicate<Product> filter ,Comparator<Product> sorter) {

        StringBuilder txt = new StringBuilder();

        txt.append(products.keySet().stream()
                .sorted(sorter)
                .filter(filter)
                .map(p -> formatter.formatProduct(p) + '\n')
                .collect(Collectors.joining()));
        System.out.print(txt);
    }

    public void parseReview (String text) {
        try {
            Object[] values = reviewFormat.parse(text);
            reviewProduct(Integer.parseInt((String)values[0]), Rateable.convert(Integer.parseInt((String) values[1])), (String) values[2]);
        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review" + text + " " + e.getMessage());
        }
    }

    public void parseProduct(String text) {
        try {
            Object[] values = producFormat.parse(text);
            int id = Integer.parseInt((String)values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            switch ((String) values[0]) {
                case "D" :
                    createProduct(id,name,price,rating);
                    break;
                case "F" :
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    createProduct(id,name,price,rating,bestBefore);
            }

        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING , "Error parsing product" + text + " " + e.getMessage());
        }
    }


    public Map<String, String> getDiscounts() {

        return products.keySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                product -> product.getRating().getStars(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(
                                                product -> product.getDiscount().doubleValue()),
                                        discount -> formatter.numberFormat.format(discount))));
    }


}
