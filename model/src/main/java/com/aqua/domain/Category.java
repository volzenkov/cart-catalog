package com.aqua.domain;

import javax.persistence.*;


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

}
