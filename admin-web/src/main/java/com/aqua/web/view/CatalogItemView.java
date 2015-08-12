package com.aqua.web.view;

import com.aqua.domain.AttributeDef;
import com.aqua.domain.AttributeValue;
import com.aqua.domain.CatalogItem;
import com.aqua.domain.Category;
import com.aqua.web.controller.CatalogItemController;
import org.primefaces.model.TreeNode;
import org.w3c.dom.Attr;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kama3 on 29.06.2015.
 */
@ManagedBean(name = "catalogItemView")
@SessionScoped
public class CatalogItemView implements Serializable {

    private CatalogItem newCatalogItem;
    private TreeNode newCatalogItemParent;

    private AttributeDef newCatalogItemAttributeDef;

    private List<CatalogItem> catalogItems;
    private List<CatalogItem> selectedCatalogItems;

    @ManagedProperty("#{catalogItemController}")
    private CatalogItemController catalogItemController;

    @PostConstruct
    public void init() {
        initNewCatalogItemDialog();
        initCatalogItemsList();
    }

    public void initNewCatalogItemDialog() {
        newCatalogItem = new CatalogItem();
    }

    public void initAddCatalogItemAttributeDialog() {
        newCatalogItemAttributeDef = new AttributeDef();
    }

    public void initCatalogItemsList() {
        catalogItems = catalogItemController.listCatalogItems();
    }

    public void addCatalogItem() {
        CatalogItem newCatalogItem = this.getNewCatalogItem();
        if (newCatalogItem != null) {
            if (newCatalogItemParent != null) {
                newCatalogItem.setParent((Category) newCatalogItemParent.getData());
            }
            catalogItemController.addCatalogItem(newCatalogItem);
            initCatalogItemsList();
        }
    }

    public void addCatalogItemAttribute() {
        CatalogItem newCatalogItem = this.getNewCatalogItem();
        if (newCatalogItem != null) {
            if (newCatalogItemAttributeDef != null) {
                newCatalogItem.getAttributeValues().add(new AttributeValue(newCatalogItemAttributeDef, newCatalogItem, null));
            }
        }
    }

    public void initEditCatalogItemDialog() {
        if (this.getSelectedCatalogItems() != null && this.getSelectedCatalogItems().size() > 0) {
            this.newCatalogItem = this.getSelectedCatalogItems().get(0);
        }
    }

    public CatalogItem getNewCatalogItem() {
        return newCatalogItem;
    }

    public void setNewCatalogItem(CatalogItem newCatalogItem) {
        this.newCatalogItem = newCatalogItem;
    }

    public CatalogItemController getCatalogItemController() {
        return catalogItemController;
    }

    public List<CatalogItem> getCatalogItems() {
        return catalogItems;
    }

    public void setCatalogItemController(CatalogItemController catalogItemController) {
        this.catalogItemController = catalogItemController;
    }

    public TreeNode getNewCatalogItemParent() {
        return newCatalogItemParent;
    }

    public void setNewCatalogItemParent(TreeNode newCatalogItemParent) {
        this.newCatalogItemParent = newCatalogItemParent;
    }

    public void setCatalogItems(List<CatalogItem> catalogItems) {
        this.catalogItems = catalogItems;
    }

    public List<CatalogItem> getSelectedCatalogItems() {
        return selectedCatalogItems;
    }

    public void setSelectedCatalogItems(List<CatalogItem> selectedCatalogItems) {
        this.selectedCatalogItems = selectedCatalogItems;
    }

    public AttributeDef getNewCatalogItemAttributeDef() {
        return newCatalogItemAttributeDef;
    }

    public void setNewCatalogItemAttributeDef(AttributeDef newCatalogItemAttributeDef) {
        this.newCatalogItemAttributeDef = newCatalogItemAttributeDef;
    }
}
