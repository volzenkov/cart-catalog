package com.aqua.dao.listeners;

import com.aqua.domain.CatalogNode;
import com.aqua.domain.Category;
import com.aqua.domain.PersistentEntity;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultSaveEventListener;

import java.util.Date;

public class SaveDateListener extends DefaultSaveEventListener {

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) {
        if (event.getObject() instanceof PersistentEntity) {
            PersistentEntity persistentEntity = (PersistentEntity) event.getObject();
            Date created = new Date();
            persistentEntity.setCreated(created);
            persistentEntity.setUpdated(created);
            if (event.getObject() instanceof CatalogNode) {
                CatalogNode catalogNode = (CatalogNode) event.getObject();
                if (catalogNode.getParent() != null) {
                    String parentNumericPath = catalogNode.getParent().getId() + ".";
                    if (catalogNode.getParent().getParentNumericPath() != null) {
                        parentNumericPath = catalogNode.getParent().getParentNumericPath() + parentNumericPath;
                    }
                    catalogNode.setParentNumericPath(parentNumericPath);
                }
            }
        }
        super.onSaveOrUpdate(event);
    }
}

