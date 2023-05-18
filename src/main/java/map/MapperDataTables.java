package map;

import queryBuilder.QueryBuilder;
import java.util.HashMap;
import java.util.List;

public class MapperDataTables {

    public static void mapProductsTable(List<HashMap<String, List<String>>> data) {
        for (HashMap<String, List<String>> hashMap : data) {
            List<String> asinList = hashMap.get("asin");
            List<String> pTitleList = hashMap.get("ptitle");
            List<String> pGroupList = hashMap.get("pgroup");
            List<String> eanList = hashMap.get("ean");
            List<String> imageUrlList = hashMap.get("image_url");
            List<String> detailPageUrlList = hashMap.get("detailpage_url");
            List<String> salesRankList = hashMap.get("salesrank");
            List<String> upcList = hashMap.get("upc");

            int maxListSize = getMaxListSize(asinList, pTitleList, pGroupList, eanList,
                    imageUrlList, detailPageUrlList, salesRankList, upcList);

            for (int i = 0; i < maxListSize; i++) {
                String tableName = "products";
                String[] columns = {"asin", "ptitle", "pgroup", "ean", "image_url", "detailpage_url", "salesrank", "upc"};
                Object[] values = {
                        getValueAtIndex(asinList, i),
                        getValueAtIndex(pTitleList, i),
                        getValueAtIndex(pGroupList, i),
                        getValueAtIndex(eanList, i),
                        getValueAtIndex(imageUrlList, i),
                        getValueAtIndex(detailPageUrlList, i),
                        parseSalesRank(getValueAtIndex(salesRankList, i)),
                        getValueAtIndex(upcList, i)
                };

                String sql = QueryBuilder.buildInsertStatement(tableName, columns, values);
                System.out.println(sql);
            }
        }
    }

    private static int getMaxListSize(List<?>... lists) {
        int maxListSize = 0;
        for (List<?> list : lists) {
            if (list != null && list.size() > maxListSize) {
                maxListSize = list.size();
            }
        }
        return maxListSize;
    }

    private static Object getValueAtIndex(List<?> list, int index) {
        if (list != null && index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }

    private static Integer parseSalesRank(Object value) {
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                // Handle parsing error, return null or throw an exception as per your requirements
            }
        }
        return null;
    }
}
