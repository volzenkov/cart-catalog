package com.aqua.dao.impl;

import com.aqua.dao.CatalogItemDAO;
import com.aqua.domain.CatalogItem;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CatalogItemDAOImpl extends GenericDAOImpl<CatalogItem> implements CatalogItemDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void addCatalogItem(CatalogItem CatalogItem) {
        sessionFactory.getCurrentSession().save(CatalogItem);
    }

    @Override
    public void updateCatalogItem(CatalogItem CatalogItem) {
        sessionFactory.getCurrentSession().merge(CatalogItem);
    }

    public List<CatalogItem> listCatalogItems() {

        return sessionFactory.getCurrentSession().createQuery("from CatalogItem").list();
    }

    @Override
    public List<CatalogItem> listCatalogItemsByParentId(Long parentId) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery("getCategoriesByParentId");
        query.setLong("parentId", parentId);
        List<CatalogItem> results = query.list();

        return results;
    }

    public void removeCatalogItem(Long id) {
        CatalogItem CatalogItem = (CatalogItem) sessionFactory.getCurrentSession().load(CatalogItem.class, id);
        if (null != CatalogItem) {
            List<CatalogItem> CatalogItems = listCatalogItemsByParentId(CatalogItem.getId());
            for (CatalogItem item : CatalogItems) {
                listCatalogItemsByParentId(item.getId());
                for (CatalogItem item1 : CatalogItems) {
                    sessionFactory.getCurrentSession().delete(item1);
                }
                sessionFactory.getCurrentSession().delete(item);
            }
            sessionFactory.getCurrentSession().delete(CatalogItem);
        }

    }
}
