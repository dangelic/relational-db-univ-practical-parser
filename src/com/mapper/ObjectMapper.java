package com.mapper;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class ObjectMapper {
    private static final String CONFIG_FILE = "./src/com/mapper/map.json";
    private static final String JSON_FILE_1 = "./etl/parsed_to_json_unmapped/leipzig_parsed_unmapped.json";
    private static final String JSON_FILE_2 = "./etl/parsed_to_json_unmapped/dresden_parsed_unmapped.json";

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.mapAttributes();
    }

    public void mapAttributes() {
        try {
            // Read the configuration file
            List<AttributeMapping> attributeMappings = readConfigFile(CONFIG_FILE);

            // Read the JSON files
            JsonObject json1 = readJSONFile(JSON_FILE_1);
            JsonObject json2 = readJSONFile(JSON_FILE_2);

            // Perform attribute mapping
            JsonObject mappedJson1 = mapAttributesInJSON(json1, attributeMappings);
            JsonObject mappedJson2 = mapAttributesInJSON(json2, attributeMappings);

            // Write the mapped JSON files
            writeJSONFile(mappedJson1, "mapped_file1.json");
            writeJSONFile(mappedJson2, "mapped_file2.json");

            System.out.println("Attribute mapping completed successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }

    private List<AttributeMapping> readConfigFile(String filename) throws IOException {
        try (FileReader fileReader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type attributeMappingListType = TypeToken.getParameterized(List.class, AttributeMapping.class).getType();
            return gson.fromJson(fileReader, attributeMappingListType);
        }
    }

    private JsonObject readJSONFile(String filename) throws IOException {
        try (FileReader fileReader = new FileReader(filename)) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(fileReader);
            return jsonElement.getAsJsonObject();
        }
    }

    private JsonObject mapAttributesInJSON(JsonObject json, List<AttributeMapping> attributeMappings) {
        JsonObject mappedJson = new JsonObject();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            AttributeMapping mapping = findAttributeMapping(attributeMappings, key);

            String newKey = (mapping != null) ? mapping.getReplaceAttribute() : key;
            mappedJson.add(newKey, value);

            if (mapping != null) {
                System.out.println("Mapped attribute: " + key + " -> " + newKey);
            }
        }

        return mappedJson;
    }

    private AttributeMapping findAttributeMapping(List<AttributeMapping> attributeMappings, String searchAttribute) {
        for (AttributeMapping mapping : attributeMappings) {
            if (mapping.getSearchAttribute().equals(searchAttribute)) {
                return mapping;
            }
        }
        return null;
    }

    private void writeJSONFile(JsonObject json, String filename) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileWriter.write(gson.toJson(json));
        }
    }

    public class AttributeMapping {
        private String searchAttribute;
        private String replaceAttribute;

        public String getSearchAttribute() {
            return searchAttribute;
        }

        public String getReplaceAttribute() {
            return replaceAttribute;
        }
    }}
