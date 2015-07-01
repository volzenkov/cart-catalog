package com.aqua.services.tree;

public interface Visitable<T> {

    void accept(Visitor<T> visitor);
}
