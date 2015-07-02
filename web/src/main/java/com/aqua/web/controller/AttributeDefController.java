package com.aqua.web.controller;

import com.aqua.domain.AttributeDef;
import com.aqua.domain.AttributeType;
import com.aqua.services.AttributeDefService;
import com.aqua.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;

@Component
@ManagedBean(name = "attributeDefController")
@ApplicationScoped
public class AttributeDefController {

    @Autowired
    private AttributeDefService attributeDefService;

    public AttributeType[] getAttributeTypes() {
        return AttributeType.values();
    }
    public void addAttributeDef(AttributeDef attributeDef) {
        attributeDefService.save(attributeDef);
    }

    public List<AttributeDef> listAttributeDefs() {
        return attributeDefService.listAttributeDefs();
    }

    public void setAttributeDefService(AttributeDefService attributeDefService) {
        this.attributeDefService = attributeDefService;
    }
}