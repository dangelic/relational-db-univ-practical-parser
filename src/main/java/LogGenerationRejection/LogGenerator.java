package LogGenerationRejection;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.time.Month;


public class LogGenerator {
    private static final Lock lock = new ReentrantLock();

    public static void check(List<HashMap<String, List<String>>> parsedXMLDataProducts, List<HashMap<String, List<String>>> parsedCSVDataReviews) {
        int failureCount = 0; // Counter to keep track of failures

        System.out.println(parsedCSVDataReviews);


        for (HashMap<String, List<String>> hashMap : parsedXMLDataProducts) failureCount += checkMissingAsin(hashMap);
        for (HashMap<String, List<String>> hashMap : parsedXMLDataProducts) failureCount += checkMissingTitleInProperTags(hashMap);
        for (HashMap<String, List<String>> hashMap : parsedXMLDataProducts) failureCount += checkEANLength(hashMap);
        for (HashMap<String, List<String>> hashMap : parsedXMLDataProducts) failureCount += checkUPCEntries(hashMap);
        for (HashMap<String, List<String>> hashMap : parsedXMLDataProducts) failureCount += checkEmptyPgroup(hashMap);
        failureCount += checkDuplicateProductUserReview(parsedCSVDataReviews);
        failureCount += checkASINLengthInReviews(parsedCSVDataReviews);
        failureCount += checkInvalidReviewDate(parsedCSVDataReviews);
        failureCount += checkInvalidRating(parsedCSVDataReviews);


        System.out.println("Total failures: " + failureCount);
    }

    private static int checkMissingAsin(HashMap<String, List<String>> hashMap) {
        int failureCount = 0; // Counter for this check method

        if (hashMap.containsKey("asin")) {
            List<String> asinList = hashMap.get("asin");
            if (asinList.isEmpty()) {
                log(getAsinString(hashMap) + " Empty 'asin' found!");
                failureCount++;
            }}

        return failureCount;
    }


    private static int checkMissingTitleInProperTags(HashMap<String, List<String>> hashMap) {
        int failureCount = 0; // Counter for this check method

        List<String> titleList = hashMap.get("title");
        if (titleList == null || titleList.isEmpty()) {
            List<String> asinList = hashMap.get("asin");
            if (asinList != null && !asinList.isEmpty()) {
                String asin = asinList.get(0);
                log(getAsinString(hashMap) + " Missing or empty 'title' attribute found!");
                failureCount++;
            } else {
                log(getAsinString(hashMap) + " Missing or empty 'title' attribute found! No 'asin' attribute available.");
                failureCount++;
            }
        }

        return failureCount;
    }

    private static int checkEANLength(HashMap<String, List<String>> hashMap) {
        int failureCount = 0; // Counter for this check method

        List<String> eanList = hashMap.get("ean");
        if (eanList != null && !eanList.isEmpty()) {
            String ean = eanList.get(0);
            if (ean.length() < 14) {
                log(getAsinString(hashMap) + " EAN length is smaller than 14! EAN: " + ean);
                failureCount++;
            }
        }

        return failureCount;
    }

    private static int checkUPCEntries(HashMap<String, List<String>> hashMap) {
        int failureCount = 0; // Counter for this check method

        List<String> upcList = hashMap.get("upc");
        if (upcList != null && !upcList.isEmpty()) {
            String upc = upcList.get(0);
            if (upc.length() > 13) {
                log(getAsinString(hashMap) + " UPC has multiple entries! UPC: " + upc);
                failureCount++;
            }
        }

        return failureCount;
    }

    private static int checkEmptyPgroup(HashMap<String, List<String>> hashMap) {
        int failureCount = 0; // Counter for this check method

        List<String> pgroupList = hashMap.get("pgroup");
        List<String> shopidList = hashMap.get("shop_id");
        if (pgroupList != null && pgroupList.isEmpty()) {
            if (shopidList.get(0).equals("LEIPZIG"))  log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + " Empty 'pgroup' attribute found!");
            else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + " Empty 'pgroup' attribute found!");
            failureCount++;
        }

        return failureCount;
    }

    private static int checkDuplicateProductUserReview(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method
        HashSet<String> productUserSet = new HashSet<>();

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> productList = hashMap.get("product");
            List<String> userList = hashMap.get("user");

            if (productList != null && !productList.isEmpty() && userList != null && !userList.isEmpty() && !userList.get(0).equals("guest")) {
                for (int i = 0; i < productList.size(); i++) {
                    String productUser = productList.get(i) + userList.get(i);
                    if (!productUserSet.add(productUser)) {
                        log(getAsinStringReviews(hashMap) + "[DATA: 'reviews.csv']" + " User reviewed the same product more than once! Username: " + userList.get(i));
                        failureCount++;
                    }
                }
            }
        }
        return failureCount;
    }

    private static int checkASINLengthInReviews(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> productList = hashMap.get("product");

            if (productList != null && !productList.isEmpty()) {
                for (String product : productList) {
                    if (product.length() < 9) {
                        log(getAsinStringReviews(hashMap) + "[DATA: 'reviews.csv']" + " ASIN length is not valid!");
                        failureCount++;
                    }
                }
            }
        }
        return failureCount;
    }


    private static int checkInvalidReviewDate(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        LocalDate minDate = LocalDate.of(1994, 7, 5);

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> reviewDateList = hashMap.get("reviewdate");

            if (reviewDateList != null && !reviewDateList.isEmpty()) {
                for (String reviewDate : reviewDateList) {
                    LocalDate date = LocalDate.parse(reviewDate);

                    if (date.isBefore(minDate)) {
                        log(getAsinStringReviews(hashMap) + "[DATA: 'reviews.csv']" + " Invalid review date compared to founding date of Amazon! Date: " + reviewDate);
                        failureCount++;
                    }
                }
            }
        }
        return failureCount;
    }

    private static int checkInvalidRating(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> ratingList = hashMap.get("rating");
            List<String> userList = hashMap.get("user");

            if (ratingList != null && !ratingList.isEmpty() && userList != null && !userList.isEmpty()) {
                for (int i = 0; i < ratingList.size(); i++) {
                    String rating = ratingList.get(i);
                    String user = userList.get(i);

                    try {
                        int ratingValue = Integer.parseInt(rating);

                        if (ratingValue > 5) {
                            log(getAsinStringReviews(hashMap) + "[DATA: 'reviews.csv']" + " Product rating is too high! Rating: " + rating + ", User: " + user);
                            failureCount++;
                        }
                    } catch (NumberFormatException e) {
                        // Handle invalid rating format (not an integer)
                        log(getAsinStringReviews(hashMap) + "[DATA: 'reviews.csv']" + " Invalid rating format! Rating: " + rating + ", User: " + user);
                        failureCount++;
                    }
                }
            }
        }

        return failureCount;
    }








    private static String getAsinString(HashMap<String, List<String>> hashMap) {
        List<String> asinList = hashMap.get("asin");
        if (asinList != null && !asinList.isEmpty()) {
            return "[ASIN: " + asinList.get(0) + "] ";
        } else {
            return "[ASIN: NO ASIN FOUND] ";
        }
    }

    private static String getAsinStringReviews(HashMap<String, List<String>> hashMap) {
        List<String> asinList = hashMap.get("product");
        if (asinList != null && !asinList.isEmpty()) {
            return "[ASIN: " + asinList.get(0) + "] ";
        } else {
            return "[ASIN: NO ASIN FOUND] ";
        }
    }

    private static void log(String message) {
        lock.lock();
        try {
            System.out.println(message);
        } finally {
            lock.unlock();
        }
    }
}
