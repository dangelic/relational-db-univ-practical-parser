package dataCleanUp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CleanUpOperations {
    public static List<HashMap<String, List<String>>> replaceMissingCharacters(List<HashMap<String, List<String>>> hashmapList, String replacementAttribute) {
        List<HashMap<String, List<String>>> replacedList = new ArrayList<>();

        // Iterate over the hashmapList
        for (HashMap<String, List<String>> entry : hashmapList) {
            HashMap<String, List<String>> replacedEntry = new HashMap<>(entry);
            List<String> asinValues = entry.get("asin");

            // Check if asinValues exist and have at least one value
            if (asinValues != null && !asinValues.isEmpty()) {
                String asin = asinValues.get(0);
                List<String> attributeValues = entry.get(replacementAttribute);

                // Check if attributeValues exist and have at least one value
                if (attributeValues != null && !attributeValues.isEmpty()) {
                    List<String> replacedAttributeValues = new ArrayList<>();

                    // Iterate over the attribute values
                    for (String originalValue : attributeValues) {
                        String replacedValue = originalValue;

                        // Find the matching entry with the same asin and search for values with vowels
                        for (HashMap<String, List<String>> comparisonEntry : hashmapList) {
                            List<String> comparisonAsinValues = comparisonEntry.get("asin");

                            // Skip the comparison if asinValues are null or do not match
                            if (comparisonAsinValues == null || comparisonAsinValues.isEmpty() || !comparisonAsinValues.get(0).equals(asin)) {
                                continue;
                            }

                            List<String> comparisonAttributeValues = comparisonEntry.get(replacementAttribute);

                            // Skip the comparison if comparisonAttributeValues are null or do not have at least one value
                            if (comparisonAttributeValues == null || comparisonAttributeValues.isEmpty()) {
                                continue;
                            }

                            // Iterate over the comparison attribute values
                            for (String comparisonValue : comparisonAttributeValues) {
                                if (matchesVowelString(comparisonValue) && matchesNonVowelString(originalValue, comparisonValue)) {
                                    // Replace the original value with the matching comparison value
                                    replacedValue = comparisonValue;
                                    break;
                                }
                            }
                        }

                        replacedAttributeValues.add(replacedValue);
                    }

                    replacedEntry.put(replacementAttribute, replacedAttributeValues);
                }
            }

            replacedList.add(replacedEntry);
        }

        return replacedList;
    }

    private static boolean matchesVowelString(String value) {
        return value.matches(".*[öäüÖÄÜ].*");
    }

    private static boolean matchesNonVowelString(String originalValue, String comparisonValue) {
        // Remove vowels from the comparison value
        String comparisonValueWithoutVowels = comparisonValue.replaceAll("[öäüÖÄÜ]", "");

        return originalValue.equals(comparisonValueWithoutVowels);
    }
}

