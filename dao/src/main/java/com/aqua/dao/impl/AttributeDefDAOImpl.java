package com.aqua.dao.impl;

import com.aqua.dao.AttributeDefDAO;
import com.aqua.domain.AttributeDef;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AttributeDefDAOImpl extends GenericDAOImpl<AttributeDef> implements AttributeDefDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void addAttributeDef(AttributeDef attributeDef) {
        sessionFactory.getCurrentSession().save(attributeDef);
    }

    @Override
    public void updateAttributeDef(AttributeDef attributeDef) {
        sessionFactory.getCurrentSession().merge(attributeDef);
    }

    @Override
    public List<AttributeDef> listAttributeDefs() {
        return sessionFactory.getCurrentSession().createQuery("from AttributeDef").list();
    }

    public void removeAttributeDef(Long id) {
        AttributeDef AttributeDef = (AttributeDef) sessionFactory.getCurrentSession().load(AttributeDef.class, id);
        if (null != AttributeDef) {
            sessionFactory.getCurrentSession().delete(AttributeDef);
        }
    }
}
