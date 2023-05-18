package fieldMapper;

import java.util.HashMap;

public class TableFieldMappings {
    public HashMap<String, String> getProductEntityFieldMappings() {
        HashMap<String, String> productEntityFieldMapping = new HashMap<>();
        productEntityFieldMapping.put("asin", "asin@string");
        productEntityFieldMapping.put("pgroup", "string");
        productEntityFieldMapping.put("salesrank", "integer");
        return productEntityFieldMapping;
    }
}
