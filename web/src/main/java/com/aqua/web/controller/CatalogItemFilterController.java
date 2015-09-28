package com.aqua.web.controller;

import com.aqua.domain.Category;
import com.aqua.services.CatalogItemFilter;
import com.aqua.services.CatalogItemFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

@Component
@ManagedBean(name = "catalogItemFilterController")
@ApplicationScoped
public class CatalogItemFilterController {

    @Autowired
    private CatalogItemFilterService catalogItemFilterService;

    public List<CatalogItemFilter> listCatalogItemFilters() {
        return catalogItemFilterService.getCatalogItemFiltersRegistry();
    }

    public List<CatalogItemFilter> listCatalogItemFilters(Category category) {
        return catalogItemFilterService.getFiltersByCategory(category);
    }

    public void setCatalogItemFilterService(CatalogItemFilterService catalogItemFilterService) {
        this.catalogItemFilterService = catalogItemFilterService;
    }
}