package com.aqua.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "getCatalogItemsByParentId", query = "from CatalogItem item where item.parent.id = :parentId")
})

@Entity
@Table(name = "catalog_item")
@Inheritance(strategy = InheritanceType.JOINED)
public class CatalogItem extends CatalogNode {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "catalog_item_id_fk")
    private List<AttributeValue> attributeValues = new ArrayList<>();

    public CatalogItem(String name) {
        super(name);
    }

    public CatalogItem() {
        super();
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
