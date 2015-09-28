package com.aqua.services;

import com.aqua.domain.AttributeDef;
import com.aqua.domain.CatalogItem;
import com.aqua.domain.Category;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class CatalogItemFilterService {

    @Autowired
    private BaseCRUDHelper baseCRUDHelper;

    @Autowired
    private SessionFactory sessionFactory;

    private List<CatalogItemFilter> catalogItemFiltersRegistry = new ArrayList<>();

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

    @Transactional(readOnly = true)
    public List<CatalogItemFilter> getFiltersByCategory(Category category) {
        List<CatalogItemFilter> result = new LinkedList<>();

        String parentNumericPath = category.getId() + ".%";
        if (category.getParentNumericPath() != null) {
            parentNumericPath = category.getParentNumericPath() + parentNumericPath;
        }

        List<AttributeDef> attributeDefsByCategory = baseCRUDHelper.executeNamedQueryWithResult(
                "getDistinctAttributeDefByParentPath", new Object[]{parentNumericPath});

        if (CollectionUtils.isNotEmpty(attributeDefsByCategory)) {

            for (AttributeDef attributeDef : attributeDefsByCategory) {
                List<String> values = baseCRUDHelper.executeNamedQueryWithResult(
                        "getDistinctAttributeValuesByAttributeDefAndCategory", new Object[]{attributeDef.getId(), parentNumericPath});

                result.add(new CatalogItemFilter(attributeDef, values.toArray(new String[values.size()])));
            }

        }
        return result;
    }

    public void setBaseCRUDHelper(BaseCRUDHelper baseCRUDHelper) {
        this.baseCRUDHelper = baseCRUDHelper;
    }

    public List<CatalogItemFilter> getCatalogItemFiltersRegistry() {
        return catalogItemFiltersRegistry;
    }
}
