package com.aqua.dao.impl;

import com.aqua.dao.CategoryDAO;
import com.aqua.domain.Category;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CategoryDAOImpl extends GenericDAOImpl<Category> implements CategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void addCategory(Category category) {
        sessionFactory.getCurrentSession().save(category);

        category.setFullNumericPath(processFullNumericPathString(category));
        sessionFactory.getCurrentSession().save(category);
    }

    private String processFullNumericPathString(Category category) {
        if (category != null && category.getId() != null) {
            String fullNumericPath = "";
            if (category.getParent() != null) {
                fullNumericPath = category.getParent().getFullNumericPath().concat(".");
            }
            return fullNumericPath.concat(category.getId().toString());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void updateCategory(Category category) {
        category.setFullNumericPath(processFullNumericPathString(category));
        sessionFactory.getCurrentSession().merge(category);
    }

    public List<Category> listCategories() {
        return sessionFactory.getCurrentSession().createQuery("from Category").list();
    }

    @Override
    public List<Category> listCategoriesByParentId(Long parentId) {
        Query query;
        if (parentId != null) {
            query = sessionFactory.getCurrentSession().getNamedQuery("getCategoriesByParentId");
            query.setLong("parentId", parentId);
        } else {
            query = sessionFactory.getCurrentSession().getNamedQuery("getRootCategoriesItems");
        }
        List<Category> results = query.list();

        return results;
    }

    public void removeCategory(Long id) {
        Category category = (Category) sessionFactory.getCurrentSession().load(Category.class, id);
        if (null != category) {
            List<Category> categories = listCategoriesByParentId(category.getId());
            for (Category item : categories) {
                listCategoriesByParentId(item.getId());
                for (Category item1 : categories) {
                    sessionFactory.getCurrentSession().delete(item1);
                }
                sessionFactory.getCurrentSession().delete(item);
            }
            sessionFactory.getCurrentSession().delete(category);
        }

    }
}
