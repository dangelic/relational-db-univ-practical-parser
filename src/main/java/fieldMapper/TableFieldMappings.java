package fieldMapper;

import java.util.HashMap;

public class TableFieldMappings {
    public HashMap<String, String> getProductTableFieldMappings() {
        HashMap<String, String> dataTypeMapping = new HashMap<>();
        dataTypeMapping.put("asin", "asin@string");
        dataTypeMapping.put("pgroup", "string");
        dataTypeMapping.put("salesrank", "integer");
        return dataTypeMapping;
    }
}
