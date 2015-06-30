package com.aqua.dao.impl;

import com.aqua.dao.AttributeGroupDAO;
import com.aqua.domain.AttributeGroup;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AttributeGroupDAOImpl extends GenericDAOImpl<AttributeGroup> implements AttributeGroupDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void addAttributeGroup(AttributeGroup attributeGroup) {
        sessionFactory.getCurrentSession().save(attributeGroup);
    }

    @Override
    public void updateAttributeGroup(AttributeGroup attributeGroup) {
        sessionFactory.getCurrentSession().merge(attributeGroup);
    }

    @Override
    public List<AttributeGroup> listAttributeGroups() {
        return sessionFactory.getCurrentSession().createQuery("from AttributeGroup").list();
    }

    public void removeAttributeGroup(Long id) {
        AttributeGroup AttributeGroup = (AttributeGroup) sessionFactory.getCurrentSession().load(AttributeGroup.class, id);
        if (null != AttributeGroup) {
            sessionFactory.getCurrentSession().delete(AttributeGroup);
        }
    }
}
