package com.aqua.web.controller;

import com.aqua.domain.Category;
import com.aqua.services.CategoryService;
import com.aqua.services.tree.Tree;
import com.aqua.services.tree.Visitor;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@Component
@ManagedBean(name = "categoryController")
@ApplicationScoped
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    public TreeNode dummyCategories() {

        TreeNode root = new DefaultTreeNode(new Category("Root"), null);

        TreeNode node1 = new DefaultTreeNode(new Category("Node-1"), root);
        TreeNode node2 = new DefaultTreeNode(new Category("Node-2"), root);

        new DefaultTreeNode(new Category("Node-1.1"), node1);
        new DefaultTreeNode(new Category("Node-1.2"), node1);

        new DefaultTreeNode(new Category("Node-2.2"), node2);

        return root;
    }

    public TreeNode buildTree() {
        Tree<Category> categoryTree = categoryService.buildCategoriesTree();

        DefaultTreeNode root = new DefaultTreeNode(new Category("Root"), null);
        categoryTree.accept(new PrimeTreeNodeVisitor(root));

        return root;
    }

    class PrimeTreeNodeVisitor implements Visitor<Category> {

        private TreeNode parentNode;

        public PrimeTreeNodeVisitor(TreeNode parentNode) {
            this.parentNode = parentNode;
        }

        public Visitor<Category> visitTree(Tree<Category> tree) {
            return new PrimeTreeNodeVisitor(parentNode);
        }

        public void visitData(Tree<Category> parent, Category data) {
            parentNode = new DefaultTreeNode(data, parentNode);
            System.out.println(data.getName());
        }
    }

    public static void main(String[] args) {
        CategoryController categoryController = new CategoryController();
        categoryController.setCategoryService(new CategoryService());

        TreeNode treeNode = categoryController.buildTree();
        System.out.println(treeNode);

    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}