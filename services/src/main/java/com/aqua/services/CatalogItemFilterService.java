package com.aqua.services;

import com.aqua.domain.AttributeDef;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatalogItemFilterService {

    @Autowired
    private BaseCRUDHelper baseCRUDHelper;

    List<CatalogItemFilter> catalogItemFiltersRegistry = new ArrayList<>();

    @PostConstruct
    @Transactional(readOnly = true)
    public void init() {
        refreshRegistry();
    }

    public void refreshRegistry() {
        List<AttributeDef> attributeDefs = baseCRUDHelper.list(AttributeDef.class);
        for (AttributeDef attributeDef : attributeDefs) {
            List<String> valuesByAttributeDef = baseCRUDHelper.executeNamedQueryWithResult(
                    "getDistinctAttributeValuesByAttributeDef", new Object[]{attributeDef.getId()});
            if (CollectionUtils.isNotEmpty(valuesByAttributeDef)) {
                catalogItemFiltersRegistry.add(new CatalogItemFilter(
                        attributeDef, valuesByAttributeDef.toArray(new String[valuesByAttributeDef.size()])));
            }
        }
    }

    public void setBaseCRUDHelper(BaseCRUDHelper baseCRUDHelper) {
        this.baseCRUDHelper = baseCRUDHelper;
    }

    public List<CatalogItemFilter> getCatalogItemFiltersRegistry() {
        return catalogItemFiltersRegistry;
    }
}
