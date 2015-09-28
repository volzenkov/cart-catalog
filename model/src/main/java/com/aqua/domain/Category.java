package com.aqua.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NamedQueries({
        @NamedQuery(name = "getCategoriesByParentId", query = "from Category category where category.parentNumericPath like ?"),
        @NamedQuery(name = "getChildCategoriesByParentId", query = "from Category category where category.parent.id = ?"),
        @NamedQuery(name = "getRootCategoriesItems", query = "from CatalogItem item where item.parent = null"),
})

@Entity
@Table(name = "category")
@Inheritance(strategy = InheritanceType.JOINED)
public class Category extends CatalogNode {

    public Category() {}

    public Category(String name) {
        super(name);
    }

    public Category(String name, Category parentCategory) {
        super(name, parentCategory);
    }

    @Column
    @ElementCollection(targetClass=Long.class)
    private List<Long> attributeDefFilters = new ArrayList<>();

    public List<Long> getAttributeDefFilters() {
        return attributeDefFilters;
    }

    public void setAttributeDefFilters(List<Long> attributeDefFilters) {
        this.attributeDefFilters = attributeDefFilters;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
