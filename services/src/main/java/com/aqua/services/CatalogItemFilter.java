package com.aqua.services;

import com.aqua.domain.AttributeDef;

/**
 * Created by vzenkov on 06/07/15.
 */
public class CatalogItemFilter {

    private AttributeDef attributeDef;
    private String[] values;
    private String[] selectedValues;

    public CatalogItemFilter() {}

    public CatalogItemFilter(AttributeDef attributeDef, String... values) {
        this.attributeDef = attributeDef;
        this.values = values;
    }

    public AttributeDef getAttributeDef() {
        return attributeDef;
    }

    public void setAttributeDef(AttributeDef attributeDef) {
        this.attributeDef = attributeDef;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String[] getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(String[] selectedValues) {
        this.selectedValues = selectedValues;
    }
}
