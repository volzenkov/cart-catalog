package com.aqua.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order")
@Inheritance(strategy = InheritanceType.JOINED)
public class Order extends NamedEntity {

    enum PaymentType {
        CASH, CREDIT, DEBET;
    }
    @Column
    private PaymentType type;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<CatalogItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(PaymentType type) {
        this.type = type;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void setItems(List<CatalogItem> items) {
        this.items = items;
    }
}
