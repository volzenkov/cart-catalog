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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "catalog_item_id_fk")
    private List<AttributeValue> attributeValues = new ArrayList<>();

    @Embedded
    private Dimensions dimensions;

    @Embedded
    private Price price;

    @Embedded
    private Stock stock;

    private String model;

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

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
