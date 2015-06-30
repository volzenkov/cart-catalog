package com.aqua.web.controller;

import com.aqua.domain.Category;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "categoryController")
@ApplicationScoped
public class CategoryController {

    public TreeNode dummyCategories() {
        TreeNode root = new DefaultTreeNode(new Category("Root"), null);

        TreeNode node1 = new DefaultTreeNode(new Category("Node-1"), root);
        TreeNode node2 = new DefaultTreeNode(new Category("Node-2"), root);

        new DefaultTreeNode(new Category("Node-1.1"), node1);
        new DefaultTreeNode(new Category("Node-1.2"), node1);

        new DefaultTreeNode(new Category("Node-2.2"), node2);

        return root;
    }
}