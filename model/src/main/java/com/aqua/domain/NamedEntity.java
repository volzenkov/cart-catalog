package com.aqua.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by kama3 on 28.06.2015.
 */
@MappedSuperclass
public class NamedEntity extends PersistentEntity {

    @Column
    private String name;
    @Column
    private String description;

    public NamedEntity() {}

    public NamedEntity(String name) {
        this.name = name;
    }

    public NamedEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
