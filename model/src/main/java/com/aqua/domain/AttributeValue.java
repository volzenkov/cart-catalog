package com.aqua.domain;

import javax.persistence.*;

@Entity
@Table(name = "attribute_value")
@Inheritance(strategy = InheritanceType.JOINED)
public class AttributeValue extends Identity {

    @ManyToOne
    private AttributeDef attributeDef;

    @ManyToOne
    @JoinColumn(name = "catalog_item_id_fk")
    private CatalogItem catalogItem;

    @Column
    private String value;

    public AttributeValue() {}

    public AttributeValue(AttributeDef attributeDef, CatalogItem catalogItem, String value) {
        this.value = value;
        this.catalogItem = catalogItem;
        this.attributeDef = attributeDef;
    }

    public AttributeDef getAttributeDef() {
        return attributeDef;
    }

    public void setAttributeDef(AttributeDef attributeDef) {
        this.attributeDef = attributeDef;
    }

    public CatalogItem getCatalogItem() {
        return catalogItem;
    }

    public void setCatalogItem(CatalogItem catalogItem) {
        this.catalogItem = catalogItem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
