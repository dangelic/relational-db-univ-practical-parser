package LogGenerationRejection;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LogGenerator {
    private static final Lock lock = new ReentrantLock();

    public static void check(List<HashMap<String, List<String>>> parsedXMLDataProducts, List<HashMap<String, List<String>>> parsedCSVDataReviews) {
        int failureCount = 0; // Counter to keep track of failures

        System.out.println(parsedCSVDataReviews);


        checkMissingAsin(parsedXMLDataProducts);
        checkMissingTitleInProperTags(parsedXMLDataProducts);
        checkEANLength(parsedXMLDataProducts);
        checkUPCEntries(parsedXMLDataProducts);
        checkEmptyPgroup(parsedXMLDataProducts);
        checkDuplicateProductUserReview(parsedCSVDataReviews);
        checkAsinLength(parsedXMLDataProducts);
        checkASINLengthInReviews(parsedCSVDataReviews);
        checkInvalidReviewDate(parsedCSVDataReviews);
        checkInvalidRating(parsedCSVDataReviews);
        checkMissingISBN(parsedXMLDataProducts);


        System.out.println("Total failures: " + failureCount);
    }

    private static int checkMissingAsin(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            if (hashMap.containsKey("asin")) {
                List<String> asinList = hashMap.get("asin");
                List<String> shopidList = hashMap.get("shop_id");
                if (asinList.isEmpty()) {
                    if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + "Missing ASIN.");
                    else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + "Missing ASIN.");
                    failureCount++;
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Missing ASIN in datasets counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkMissingTitleInProperTags(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> titleList = hashMap.get("title");
            List<String> shopidList = hashMap.get("shop_id");
            if (titleList == null || titleList.isEmpty()) {
                List<String> asinList = hashMap.get("asin");
                if (asinList != null && !asinList.isEmpty()) {
                    if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + "Product title missing or not in proper tags.");
                    else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + "Product title missing or not in proper tags.");
                    failureCount++;
                } else {
                    if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + "Product title missing or not in proper tags. No ASIN attribute available.");
                    else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + "Product title missing or not in proper tags. No ASIN attribute available.");
                    failureCount++;
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Missing product title or invalid XML tagging in datasets counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkMissingISBN(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> pgroupList = hashMap.get("pgroup");
            List<String> isbnList = hashMap.get("bookspec_isbn");
            List<String> shopidList = hashMap.get("shop_id");

            if (pgroupList != null && !pgroupList.isEmpty() && pgroupList.get(0).equals("Book")) {
                if (isbnList == null || isbnList.isEmpty()) {
                    List<String> asinList = hashMap.get("asin");
                    if (asinList != null && !asinList.isEmpty()) {
                        if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + " Missing ISBN for a book.");
                        else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + " Missing ISBN for a book.");
                        failureCount++;
                    } else {
                        if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "Missing ISBN for a book. NO ASIN attribute available.");
                        else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + "Missing ISBN for a book. NO ASIN attribute available.");
                        failureCount++;
                    }
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Missing ISBN (for books) in datasets counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkEANLength(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> shopidList = hashMap.get("shop_id");
            List<String> eanList = hashMap.get("ean");
            if (eanList != null && !eanList.isEmpty()) {
                String ean = eanList.get(0);
                if (ean.length() != 13) {
                    if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + " EAN-13 length not 13" + ean);
                    else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + " EAN-13 length not 13" + ean);
                    failureCount++;
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Invalid EAN-length in datasets counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkUPCEntries(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> shopidList = hashMap.get("shop_id");
            List<String> upcList = hashMap.get("upc");
            if (upcList != null && !upcList.isEmpty()) {
                String upc = upcList.get(0);
                if (upc.length() > 12) {
                    if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + " Invalid UPC structure with too many entries! UPC: " + upc);
                    else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + " Invalid UPC structure with too many entries! UPC: " + upc);
                    failureCount++;
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Invalid UPC structure in datasets counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkEmptyPgroup(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> pgroupList = hashMap.get("pgroup");
            List<String> shopidList = hashMap.get("shop_id");
            if (pgroupList != null && pgroupList.isEmpty()) {
                if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + " Empty product classifier (pgroup) found!");
                else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + " Empty product classifier (pgroup) found!");
                failureCount++;
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Empty product classifier (pgroup) in datasets counted "+failureCount);
        System.out.println("--------------------------------");
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
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Duplicate reviews from the sam e user in datasets counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkASINLengthInReviews(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> productList = hashMap.get("product");

            if (productList != null && !productList.isEmpty()) {
                for (String product : productList) {
                    if (product.length() < 9) {
                        log(getAsinStringReviews(hashMap) + "[DATA: 'reviews.csv']" + " ASIN length not 10");
                        failureCount++;
                    }
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Invalid ASIN-length in datasets (only reviews.csv) counted "+failureCount);
        System.out.println("--------------------------------");
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
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Invalid reviewdate in datasets (only reviews.csv) counted "+failureCount);
        System.out.println("--------------------------------");
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
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Invalid rating in datasets (only reviews.csv) counted "+failureCount);
        System.out.println("--------------------------------");
        return failureCount;
    }

    private static int checkAsinLength(List<HashMap<String, List<String>>> data) {
        int failureCount = 0; // Counter for this check method

        for (HashMap<String, List<String>> hashMap : data) {
            List<String> asinList = hashMap.get("asin");
            List<String> shopidList = hashMap.get("shop_id");
            if (asinList != null && !asinList.isEmpty()) {
                for (String asin : asinList) {
                    if (asin.length() != 10) {
                        if (shopidList.get(0).equals("LEIPZIG")) log(getAsinString(hashMap) + "[DATA: 'leipzig_transformed.xml']" + " ASIN length not 10");
                        else log(getAsinString(hashMap) + "[DATA: 'dresden.xml']" + " ASIN length not 10");
                        failureCount++;
                    }
                }
            }
        }
        System.out.println("--------------------------------");
        System.out.println("= TOTAL: Invalid ASIN length (only shops xml) counted "+failureCount);
        System.out.println("--------------------------------");
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
