package dataCleanUp;

import java.util.*;

/**
 * The CleanUpOperations class provides various operations for data clean-up.
 */
public class CleanUpOperations {

    /**
     * Removes redundancies from a list of strings.
     *
     * @param strings The list of strings.
     * @return A new list of strings without redundancies.
     */
    public static List<String> removeRedundancies(List<String> strings) {
        List<String> distinctStrings = new ArrayList<>();
        for (String str : strings) {
            String[] splitValues = str.split(",");
            Set<String> uniqueValues = new HashSet<>();
            for (String value : splitValues) {
                String trimmedValue = value.trim();
                if (!uniqueValues.contains(trimmedValue)) {
                    uniqueValues.add(trimmedValue);
                }
            }
            String distinctString = String.join(", ", uniqueValues);
            distinctStrings.add(distinctString);
        }
        return distinctStrings;
    }

    /**
     * Splits each string in the list into individual pieces.
     *
     * @param strings The list of strings.
     * @return A new list of individual pieces.
     */
    public static List<String> splitStringIntoPieces(List<String> strings) {
        List<String> pieces = new ArrayList<>();
        for (String str : strings) {
            String[] splitValues = str.split(",");
            for (String value : splitValues) {
                pieces.add(value.trim());
            }
        }
        return pieces;
    }

    /**
     * Removes invalid DVD format strings from the input list.
     *
     * @param inputList The input list of strings.
     * @return A new list with invalid DVD format strings removed or altered.
     */
    public static List<String> removeInvalidsDvdFormat(List<String> inputList) {
        List<String> alteredList = new ArrayList<>();
        for (String str : inputList) {
            if (str.equals("Sourround Soun")) {
                str = "Sourround Sound";
            } else if (str.equals("Sourround")) {
                str = "Sourround Sound";
            } else if (str.contains("d") || str.contains("Sound")) {
                continue;
            }
            alteredList.add(str);
        }
        return alteredList;
    }

    public static List<HashMap<String, List<String>>> replaceInvalidPgroup(List<HashMap<String, List<String>>> hashmapList) {
        System.out.println("\033[34;1m INFO: Execute preprocess clean job to replace wrong pgroup declaration. Waiting for the algorithm to finish...\033[0m");
        for (HashMap<String, List<String>> hashmap : hashmapList) {
            if (hashmap.containsKey("pgroup")) {
                List<String> pgroupList = hashmap.get("pgroup");
                for (int i = 0; i < pgroupList.size(); i++) {
                    String pgroup = pgroupList.get(i);
                    if (pgroup.equals("Buch")) {
                        pgroupList.set(i, "Book");
                    }
                }
            }
        }
        return hashmapList;
    }

    /**
     * Replaces missing characters in a list of HashMap entries.
     *
     * @param hashmapList          The list of HashMap entries.
     * @param replacementAttribute The attribute to replace missing characters for.
     * @return A new list of HashMap entries with missing characters replaced.
     *
     */
    public static List<HashMap<String, List<String>>> replaceMissingCharacters(List<HashMap<String, List<String>>> hashmapList, String replacementAttribute) {
        System.out.println("\033[34;1m INFO: Execute preprocess clean job to add missing characters on products data HashMap => key: \"" + replacementAttribute + "\". Waiting for the algorithm to finish...\033[0m");

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