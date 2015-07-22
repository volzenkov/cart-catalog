package com.aqua.web.view;

import com.aqua.domain.CatalogItem;
import com.aqua.services.CatalogItemFilter;
import com.aqua.web.controller.CatalogItemController;
import com.aqua.web.controller.CatalogItemFilterController;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kama3 on 29.06.2015.
 */
@ManagedBean(name = "catalogItemFilterView")
@SessionScoped
public class CatalogItemFilterView implements Serializable {

    private List<CatalogItemFilter> catalogItemFilters;
    private List<CatalogItem> catalogItems;

    @ManagedProperty("#{catalogItemFilterController}")
    private CatalogItemFilterController catalogItemFilterController;

    @ManagedProperty("#{catalogItemController}")
    private CatalogItemController catalogItemController;

    @PostConstruct
    public void init() {
        initCatalogItemFiltersList();
    }

    public void initCatalogItemFiltersList() {
        catalogItemFilters = catalogItemFilterController.listCatalogItemFilters();
    }

    public void initCatalogItems() {
        catalogItems = catalogItemController.listCatalogItemByFilters(catalogItemFilters);
    }

    public CatalogItemController getCatalogItemController() {
        return catalogItemController;
    }

    public void setCatalogItemController(CatalogItemController catalogItemController) {
        this.catalogItemController = catalogItemController;
    }

    public CatalogItemFilterController getCatalogItemFilterController() {
        return catalogItemFilterController;
    }

    public void setCatalogItemFilterController(CatalogItemFilterController catalogItemFilterController) {
        this.catalogItemFilterController = catalogItemFilterController;
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
}
