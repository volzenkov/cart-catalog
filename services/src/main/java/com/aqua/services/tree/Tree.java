package com.aqua.services.tree;

import java.util.LinkedHashSet;
import java.util.Set;

public class Tree<T> implements Visitable<T> {

    // NB: LinkedHashSet preserves insertion order
    private final Set<Tree<T>> children = new LinkedHashSet<>();
    private final T data;

    public Tree(T data) {
        this.data = data;
    }

    public void accept(Visitor<T> visitor) {
        visitor.visitData(this, data);

        for (Tree<T> child : children) {
            Visitor<T> childVisitor = visitor.visitTree(child);
            child.accept(childVisitor);
        }
    }

    public Tree<T> child(T data) {
        for (Tree<T> child: children ) {
            if (child.data.equals(data)) {
                return child;
            }
        }

        return child(new Tree<>(data));
    }

    public Tree<T> child(Tree<T> child) {
        children.add(child);
        return child;
    }

    public T getData() {
        return data;
    }
}
