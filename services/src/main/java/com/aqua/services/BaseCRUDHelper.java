package com.aqua.services;

import com.aqua.dao.CommonDAO;
import com.aqua.dao.exceptions.CreateException;
import com.aqua.dao.exceptions.FinderException;
import com.aqua.dao.exceptions.UpdateException;
import com.aqua.domain.AttributeDef;
import com.aqua.domain.CatalogItem;
import com.aqua.domain.Identity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by vzenkov on 03/07/15.
 */
@Service
public class BaseCRUDHelper {

    @Autowired
    private CommonDAO commonDAO;

    @Transactional
    public <T extends Identity> T saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            return commonDAO.create(entity);
        } else {
            return commonDAO.update(entity);
        }
    }

    @Transactional
    public <T extends Identity> void bulkSave(Collection<T> list) {
        for (T entity : list) {
            saveOrUpdate(entity);
        }
    }

    @Transactional
    public <T extends Identity> List<T> list(Class<T> clazz) {
        try {
            return commonDAO.findAll(clazz);
        } catch (FinderException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(readOnly = true)
    public <T> List<T> executeNamedQueryWithResult(String namedQuery, Object[] parameters) {
        return commonDAO.executeNamedQueryWithResult(namedQuery, parameters);
    }

}

