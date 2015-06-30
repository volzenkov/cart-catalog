package com.aqua.dao.impl;

import com.aqua.dao.GenericDAO;
import com.aqua.dao.exceptions.CreateException;
import com.aqua.dao.exceptions.FinderException;
import com.aqua.dao.exceptions.RemoveException;
import com.aqua.dao.exceptions.UpdateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author: vzenkov
 */
@Repository
@Transactional
public class GenericDAOImpl implements GenericDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Create new record in DB
     */
    public <T> T create(T entity) throws CreateException {
        try {
            sessionFactory.getCurrentSession().save(entity);
//            sessionFactory.getCurrentSession().flush();
        } catch (Exception e) {
            throw new CreateException(e);
        }

        return entity;
    }

    /**
     * Update existent record in DB
     */
    public <T> T update(T entity) throws UpdateException {

        try {
            entity = (T) sessionFactory.getCurrentSession().merge(entity);
//            sessionFactory.getCurrentSession().flush();
        } catch (Exception e) {
            throw new UpdateException(e);
        }

        return entity;
    }

    /**
     * Delete existent record from DB
     */
    public <T> void remove(T entity) throws RemoveException {

        try {
            sessionFactory.getCurrentSession().delete(sessionFactory.getCurrentSession().merge(entity));
//            sessionFactory.getCurrentSession().flush();
        } catch (Exception e) {
            throw new RemoveException(e);
        }
    }

    /**
     * Get record from DB by primary key
     */
    public <T> T findByPrimaryKey(Class<T> clazz, Long id) {
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    public<T>  T findByPrimaryKey(Class<T> clazz, String id) {
        return (T) sessionFactory.getCurrentSession().get(clazz, id);
    }

    /**
     * Execute Named Query, which return records from DB, with given parameters.
     *
     * @throws FinderException
     */
    public <T> List<T> executeNamedQueryWithResult(String namedQuery, Object[] parameters) throws FinderException {
        try {
            Query query = sessionFactory.getCurrentSession().getNamedQuery(namedQuery);

            System.out.println("executeNamedQueryWithResult: " + namedQuery);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i + 1, parameters[i]);

                    System.out.println("parameters: " + parameters[i]);
                }
            }

            return (List<T>) query.list();
        } catch (Exception e) {
            throw new FinderException(e);
        }
    }


    /**
     * Execute Named Query, which return records from DB limits by first and max, with given parameters.
     *
     * @param namedQuery
     * @param parameters
     * @param first
     * @param max
     * @return List<T>
     * @throws FinderException
     */
    public <T> List<T> executeNamedQueryWithResult(String namedQuery, Object[] parameters, int first, int max) throws FinderException {
        try {
            Query query = sessionFactory.getCurrentSession().getNamedQuery(namedQuery);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i + 1, parameters[i]);
                }
            }

            query.setFirstResult(first);
            query.setMaxResults(max);

            return (List<T>) query.list();
        } catch (Exception e) {
            throw new FinderException(e);
        }
    }

    /**
     * Execute Named Query, which return only one record from DB, with given parameters.
     *
     * @throws FinderException
     */
    public <T> T executeNamedQueryWithOneResult(String namedQuery, Object[] parameters) throws FinderException {
        try {
            Query query = sessionFactory.getCurrentSession().getNamedQuery(namedQuery);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i + 1, parameters[i]);
                }
            }

            List<T> list = query.list();

            if (list.size() != 0)
                return list.get(0);
            else
                return null;
        } catch (Exception e) {
            throw new FinderException(e);
        }
    }

    /**
     * Execute Named Query, which doesn't return result from DB, with given parameters.
     *
     * @return the number of entities updated or deleted
     * @throws Exception
     */
    public int executeNamedQueryWithNoResult(String namedQuery, Object[] parameters) throws Exception {
        try {
            Query query = sessionFactory.getCurrentSession().getNamedQuery(namedQuery);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    query.setParameter(i + 1, parameters[i]);
                }
            }

            int result = query.executeUpdate();
            sessionFactory.getCurrentSession().flush();
            return result;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * Delete records with given ids
     *
     * @param ids - ids in format: "(id1, id2, id3,..., idn)"
     * @return number or deleted rows
     * @throws RemoveException
     */
//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> int removeByIds(Class<T> clazz, String ids) throws RemoveException {

        try {
            Query query = sessionFactory.getCurrentSession().createQuery("DELETE " + clazz.getSimpleName() + " WHERE id IN (" + ids + ")");

            return query.executeUpdate();
        } catch (Exception e) {
            throw new RemoveException(e);
        }
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) throws FinderException {
        return this.findAll(clazz, 0, 1000, null, null);
    }

    /**
     * Find all records (limit by fist and max parameters)
     *
     * @param first - the start position of the first result, numbered from 0
     * @param max   - maximum number of results to retrieve
     * @return List<T>
     * @throws FinderException
     */
//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> List<T> findAll(Class<T> clazz, int first, int max, String orderBy, String orderDirection) throws FinderException {
        try {

            String sql = "SELECT table FROM " + clazz.getSimpleName() + " table";

            if (orderBy != null) {
                sql += " ORDER BY " + orderBy;
                if (orderDirection != null) {
                    sql += " " + orderDirection;
                }
            }
            System.out.println("findAll: " + sql);

            Query query = sessionFactory.getCurrentSession().createQuery(sql);

            if (first > 0)
                query.setFirstResult(first);

            if (max > 0)
                query.setMaxResults(max);

            return (List<T>) query.list();

        } catch (Exception e) {
            throw new FinderException(e);
        }
    }

    /**
     * Find all records by list of criteria
     *
     * @param criteria - list of criteria
     * @return List<T>
     * @throws FinderException
     */
//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> List<T> getByCriteria(Class<T> clazz, Hashtable<String, String> criteria, int first, int max, String orderBy, String orderDirection) throws FinderException {

        Set<String> set = criteria.keySet();
        String key;
        String value;

        Iterator<String> it = set.iterator();

        String addition = "";

        while (it.hasNext()) {

            key = it.next();
            value = criteria.get(key);

            addition += key;

            if ((value.contains("?")) || (value.contains("%"))) {
                addition += " LIKE ";
            } else {
                addition += " = ";
            }

            addition += "'" + value + "'";

            if (it.hasNext()) {
                addition += " AND ";
            }
        }

        try {

            String sql = "SELECT table FROM " + clazz.getSimpleName() + " table WHERE " + addition;

            if (orderBy != null) {
                sql += " ORDER BY " + orderBy;
                if (orderDirection != null) {
                    sql += " " + orderDirection;
                }
            }
            System.out.println("getByCriteria: " + sql);

            Query query = sessionFactory.getCurrentSession().createQuery(sql);

            if (first > 0)
                query.setFirstResult(first);

            if (max > 0)
                query.setMaxResults(max);

            return query.list();
        } catch (Exception e) {
            throw new FinderException(e);
        }

    }

    /**
     * Count all records
     *
     * @return count of records
     * @throws
     */
//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> Long count(Class<T> clazz) {

        String sql = "SELECT COUNT(table) FROM " + clazz.getSimpleName() + " table";

        System.out.println(sql);

        Query query = sessionFactory.getCurrentSession().createQuery(sql);

        return ((Long) query.uniqueResult()).longValue();
    }


//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> T SQLQuery(String sql) throws FinderException {

        try {
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            List<T> list = query.list();
            if (list.size() != 0)
                return list.get(0);
            return null;
        } catch (Exception e) {
            throw new FinderException(e);
        }

    }

//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> List<T> SQLNativeQuery(String sql) throws FinderException {

        try {
            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            return (List<T>) query.list();

        } catch (Exception e) {
            throw new FinderException(e);
        }

    }

//        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> List<T> SQL(String sql) throws FinderException {
        try {

//			String sql = "SELECT table FROM " + clazz.getSimpleName() + " table";

            Query query = sessionFactory.getCurrentSession().createQuery(sql);
            List<T> list = query.list();

            return list;

        } catch (Exception e) {
            throw new FinderException(e);
        }
    }


}

