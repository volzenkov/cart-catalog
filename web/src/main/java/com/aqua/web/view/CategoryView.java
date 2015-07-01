package com.aqua.web.view;

import com.aqua.domain.Category;
import com.aqua.web.controller.CategoryController;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 * Created by kama3 on 29.06.2015.
 */
@ManagedBean(name = "categoryTreeView")
@ViewScoped
public class CategoryView implements Serializable {

    private TreeNode root;

    private Category selectedCategory;

    @ManagedProperty("#{categoryController}")
    private CategoryController categoryController;

    @PostConstruct
    public void init() {
        root = categoryController.buildTree();
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public void setCategoryController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }
}
