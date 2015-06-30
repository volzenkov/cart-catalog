package com.aqua.dao.impl;

import com.aqua.dao.AttributeValueDAO;
import com.aqua.domain.AttributeValue;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AttributeValueDAOImpl extends GenericDAOImpl<AttributeValue> implements AttributeValueDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void addAttributeValue(AttributeValue attributeValue) {
        sessionFactory.getCurrentSession().save(attributeValue);
    }

    @Override
    public void updateAttributeValue(AttributeValue attributeValue) {
        sessionFactory.getCurrentSession().merge(attributeValue);
    }

    @Override
    public List<AttributeValue> listAttributeValues() {
        return sessionFactory.getCurrentSession().createQuery("from AttributeValue").list();
    }

    public void removeAttributeValue(Long id) {
        AttributeValue AttributeValue = (AttributeValue) sessionFactory.getCurrentSession().load(AttributeValue.class, id);
        if (null != AttributeValue) {
            sessionFactory.getCurrentSession().delete(AttributeValue);
        }
    }
}
