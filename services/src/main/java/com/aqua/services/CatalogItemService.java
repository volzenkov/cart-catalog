package com.aqua.services;

import com.aqua.domain.CatalogItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogItemService {

    @Autowired
    private BaseCRUDHelper baseCRUDHelper;

    public void save(CatalogItem catalogItem) {
        baseCRUDHelper.saveOrUpdate(catalogItem);
    }

    public List<CatalogItem> list() {
        return baseCRUDHelper.list(CatalogItem.class);
    }

    public void setBaseCRUDHelper(BaseCRUDHelper baseCRUDHelper) {
        this.baseCRUDHelper = baseCRUDHelper;
    }
}
