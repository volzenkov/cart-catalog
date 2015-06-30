package com.aqua.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends NamedEntity {

    enum UserType {
        ADMIN, ANONIMOUS, CUSTOMER;
    }

    @Column
    private UserType type;

    public User() {
    }

    public User(String name, UserType type) {
        super(name);
        this.type = type;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
