package com.aqua.web.view;

import com.aqua.domain.AttributeDef;
import com.aqua.web.controller.AttributeDefController;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.util.List;

/**
 * Created by kama3 on 29.06.2015.
 */
@ManagedBean(name = "attributeDefView")
@ViewScoped
public class AttributeDefView implements Serializable {

    private AttributeDef newAttributeDef;
    private List<AttributeDef> attributeDefs;

    @ManagedProperty("#{attributeDefController}")
    private AttributeDefController attributeDefController;

    @PostConstruct
    public void init() {
        initNewAttributeDefDialog();
        initAttributeDefsList();
    }

    public void initNewAttributeDefDialog() {
        newAttributeDef = new AttributeDef();
    }

    public void initAttributeDefsList() {
        attributeDefs = attributeDefController.listAttributeDefs();
    }

    public void addAttributeDef() {
        AttributeDef newAttributeDef = this.getNewAttributeDef();
        if (newAttributeDef != null) {
            attributeDefController.addAttributeDef(newAttributeDef);
            initAttributeDefsList();
        }
    }

    public AttributeDef getNewAttributeDef() {
        return newAttributeDef;
    }

    public void setNewAttributeDef(AttributeDef newAttributeDef) {
        this.newAttributeDef = newAttributeDef;
    }

    public AttributeDefController getAttributeDefController() {
        return attributeDefController;
    }

    public List<AttributeDef> getAttributeDefs() {
        return attributeDefs;
    }

    public void setAttributeDefController(AttributeDefController attributeDefController) {
        this.attributeDefController = attributeDefController;
    }
}
