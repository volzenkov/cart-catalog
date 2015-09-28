package com.aqua.domain;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "getAttributeDefsById",
                query = "select distinct ad from AttributeDef ad where ad.id in(?)")
})

@Entity
@Table(name = "attribute_def")
@Inheritance(strategy = InheritanceType.JOINED)
public class AttributeDef extends NamedEntity {

    @Column
    private AttributeType type;

    @Column
    private int sortingOrder;

    public AttributeDef(String name, AttributeType type) {
        super(name);
        this.type = type;
    }

    public AttributeDef() {
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }

    public int getSortingOrder() {
        return sortingOrder;
    }

    public void setSortingOrder(int sortingOrder) {
        this.sortingOrder = sortingOrder;
    }
}
