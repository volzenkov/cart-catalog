package com.aqua.services;

import com.aqua.domain.AttributeDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeDefService {

    @Autowired
    private BaseCRUDHelper baseCRUDHelper;

    public void save(AttributeDef attributeDef) {
        baseCRUDHelper.saveOrUpdate(attributeDef);
    }

    public List<AttributeDef> list() {
        return baseCRUDHelper.list(AttributeDef.class);
    }

    public void setBaseCRUDHelper(BaseCRUDHelper baseCRUDHelper) {
        this.baseCRUDHelper = baseCRUDHelper;
    }
}
