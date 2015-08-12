package com.aqua.web.controller;

import com.aqua.domain.CatalogItem;
import com.aqua.domain.AttributeType;
import com.aqua.domain.Category;
import com.aqua.services.CatalogItemFilter;
import com.aqua.services.CatalogItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

@Component
@ManagedBean(name = "catalogItemController")
@ApplicationScoped
public class CatalogItemController {

    @Autowired
    private CatalogItemService catalogItemService;

    public void addCatalogItem(CatalogItem catalogItem) {
        catalogItemService.save(catalogItem);
    }

    public List<CatalogItem> listCatalogItems() {
        return catalogItemService.list();
    }

    public List<CatalogItem> listCatalogItemByFilters(List<CatalogItemFilter> catalogItemFilters) {
        return catalogItemService.listByFilters(catalogItemFilters);
    }

    public List<CatalogItem> listCatalogItemByFilters(Category parentCategory, List<CatalogItemFilter> catalogItemFilters) {
        return catalogItemService.listByFilters(parentCategory, catalogItemFilters);
    }

    public void setCatalogItemService(CatalogItemService catalogItemService) {
        this.catalogItemService = catalogItemService;
    }
}