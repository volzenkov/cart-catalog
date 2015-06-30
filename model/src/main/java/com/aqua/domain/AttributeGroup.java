package com.aqua.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attribute_group")
@Inheritance(strategy = InheritanceType.JOINED)
public class AttributeGroup extends NamedEntity {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "attribute_group_id_fk")
    private List<AttributeDef> attributeDefs = new ArrayList<>();

    @Column
    private int sortingOrder;

    public AttributeGroup(String name) {
        super(name);
    }

    public AttributeGroup() {
        super();
    }

    public List<AttributeDef> getAttributeDefs() {
        return attributeDefs;
    }

    public void setAttributeDefs(List<AttributeDef> attributeDefs) {
        this.attributeDefs = attributeDefs;
    }

    public int getSortingOrder() {
        return sortingOrder;
    }

    public void setSortingOrder(int sortingOrder) {
        this.sortingOrder = sortingOrder;
    }
}
