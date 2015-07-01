package com.aqua.services.tree;


import com.aqua.domain.Category;

public class PrintIndentedVisitor implements Visitor<Category> {

    private final int indent;

    public PrintIndentedVisitor(int indent) {
        this.indent = indent;
    }

    public Visitor<Category> visitTree(Tree<Category> tree) {
        return new PrintIndentedVisitor(indent + 2);
    }

    public void visitData(Tree<Category> parent, Category data) {
        for (int i = 0; i < indent; i++) { // TODO: naive implementation
            System.out.print(" ");
        }
        System.out.println(data.getName());
    }
}
