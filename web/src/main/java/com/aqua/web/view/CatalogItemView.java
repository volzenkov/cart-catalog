package com.aqua.web.view;

import com.aqua.domain.CatalogItem;
import com.aqua.domain.Category;
import com.aqua.services.CatalogItemFilter;
import com.aqua.web.controller.CatalogItemController;
import com.aqua.web.controller.CatalogItemFilterController;
import com.aqua.web.controller.CategoryController;
import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Created by kama3 on 29.06.2015.
 */
@ManagedBean(name = "catalogItemView")
@SessionScoped
public class CatalogItemView implements Serializable {

    private TreeNode categoriesTree;
    private TreeNode selectedNode;
//    private MenuModel baseCategoriesMenu;
    private List<Category> baseCategories;
    private Category selectedBaseCategory;
    private List<CatalogItem> catalogItems;

    private List<CatalogItemFilter> catalogItemFilters;

    @ManagedProperty("#{catalogItemController}")
    private CatalogItemController catalogItemController;

    @ManagedProperty("#{categoryController}")
    private CategoryController categoryController;

    @ManagedProperty("#{catalogItemFilterController}")
    private CatalogItemFilterController catalogItemFilterController;

    @PostConstruct
    public void init() {
        baseCategories = categoryController.getChildCategories(1);
        selectedBaseCategory = baseCategories.get(0);
        initCategoriesTree();
        initCatalogItems();
        initCatalogItemFiltersList();
        selectedNode = categoriesTree;
//        baseCategoriesMenu = new DefaultMenuModel();
//        for (Category baseCategory : baseCategories) {
//            DefaultMenuItem defaultMenuItem = new DefaultMenuItem(baseCategory.getName());
//            defaultMenuItem.setCommand("#{mantClienteMB.save}");
//            baseCategoriesMenu.addElement(defaultMenuItem);
//        }
    }

    public void initCatalogItemFiltersList() {
        //catalogItemFilters = catalogItemFilterController.listCatalogItemFilters();
        catalogItemFilters = catalogItemFilterController.listCatalogItemFilters(selectedBaseCategory);
    }

    public void initCatalogItems() {
        catalogItems = catalogItemController.listCatalogItemByFilters(selectedBaseCategory, catalogItemFilters);
    }

    public void initCategoriesTree() {
        categoriesTree = categoryController.buildTree(selectedBaseCategory);
    }

    public TreeNode getCategoriesTree() {
        return categoriesTree;
    }

    public void setCategoriesTree(TreeNode categoriesTree) {
        this.categoriesTree = categoriesTree;
    }

    public List<Category> getBaseCategories() {
        return baseCategories;
    }

    public void setBaseCategories(List<Category> baseCategories) {
        this.baseCategories = baseCategories;
    }

    public Category getSelectedBaseCategory() {
        return selectedBaseCategory;
    }

    public void setSelectedBaseCategory(Category selectedBaseCategory) {
        this.selectedBaseCategory = selectedBaseCategory;
        initCategoriesTree();
        initCatalogItems();
        initCatalogItemFiltersList();
    }

    public List<CatalogItem> getCatalogItems() {
        return catalogItems;
    }

    public void setCatalogItems(List<CatalogItem> catalogItems) {
        this.catalogItems = catalogItems;
    }

    public List<CatalogItemFilter> getCatalogItemFilters() {
        return catalogItemFilters;
    }

    public void setCatalogItemFilters(List<CatalogItemFilter> catalogItemFilters) {
        this.catalogItemFilters = catalogItemFilters;
    }

    public CatalogItemController getCatalogItemController() {
        return catalogItemController;
    }

    public void setCatalogItemController(CatalogItemController catalogItemController) {
        this.catalogItemController = catalogItemController;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public void setCategoryController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    public CatalogItemFilterController getCatalogItemFilterController() {
        return catalogItemFilterController;
    }

    public void setCatalogItemFilterController(CatalogItemFilterController catalogItemFilterController) {
        this.catalogItemFilterController = catalogItemFilterController;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        handleCategoryTreeNodeEvent(event.getTreeNode());
    }

    public void onNodeExpand(NodeExpandEvent event) {
        handleCategoryTreeNodeEvent(event.getTreeNode());
    }

    private void handleCategoryTreeNodeEvent(TreeNode treeNode) {
        Object data = treeNode.getData();
        if (data instanceof Category) {
            Category category = (Category) data;
            catalogItemFilters = catalogItemFilterController.listCatalogItemFilters(category);
            catalogItems = catalogItemController.listCatalogItemByFilters(category, catalogItemFilters);
            if (treeNode.isLeaf()) {
                List<String> consumersByCategory = categoryController.getConsumersByCategory(category);
                for (String s : consumersByCategory) {
                    treeNode.getChildren().add(new DefaultTreeNode(s));
                }
            }
        }
    }
}
