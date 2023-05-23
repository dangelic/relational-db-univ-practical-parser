package parserCategoriesXML;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String name;
    private List<String> items;
    private List<Category> subCategories;

    public Category() {
        this.items = new ArrayList<>();
        this.subCategories = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void addItem(String item) {
        items.add(item);
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void addSubCategory(Category subCategory) {
        subCategories.add(subCategory);
    }
}
