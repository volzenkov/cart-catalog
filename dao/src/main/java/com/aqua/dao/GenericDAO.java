package com.aqua.dao;

import com.aqua.dao.exceptions.CreateException;
import com.aqua.dao.exceptions.FinderException;
import com.aqua.dao.exceptions.RemoveException;
import com.aqua.dao.exceptions.UpdateException;

import java.util.Hashtable;
import java.util.List;

/**
 * @author: vzenkov
 */
public interface GenericDAO {
    
    /**
     * Create new record in DB
     */
    public <T> T create(T entity) throws CreateException;

    /**
     * Update existent record in DB
     */
    public <T> T update(T entity) throws UpdateException;

    /**
     * Delete existent record from DB
     */
    public <T> void remove(T entity) throws RemoveException, RemoveException;

    /**
     * Get record from DB by primary key
     */
    public <T> T findByPrimaryKey(Class<T> clazz, Long id);
    public <T> T findByPrimaryKey(Class<T> clazz, String id);

    /**
     * Execute Named Query, which return records from DB, with given parameters.
     *
     * @throws FinderException
     */
    public <T> List<T> executeNamedQueryWithResult(String namedQuery, Object[] parameters) throws FinderException;

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
    public <T> List<T> executeNamedQueryWithResult(String namedQuery, Object[] parameters, int first, int max) throws FinderException;

    /**
     * Execute Named Query, which return only one record from DB, with given parameters.
     *
     * @throws FinderException
     */
    public <T> T executeNamedQueryWithOneResult(String namedQuery, Object[] parameters) throws FinderException;

    /**
     * Execute Named Query, which doesn't return result from DB, with given parameters.
     *
     * @return the number of entities updated or deleted
     * @throws Exception
     */
    public int executeNamedQueryWithNoResult(String namedQuery, Object[] parameters) throws Exception;

    /**
     * Delete records with given ids
     *
     * @param ids - ids in format: "(id1, id2, id3,..., idn)"
     * @return number or deleted rows
     * @throws RemoveException
     */
    public <T> int removeByIds(Class<T> clazz, String ids) throws RemoveException;

    /**
     * Find all records (limit by fist and max parameters)
     *
     * @param first - the start position of the first result, numbered from 0
     * @param max   - maximum number of results to retrieve
     * @return List<T>
     * @throws FinderException
     */
    public <T> List<T> findAll(Class<T> clazz) throws FinderException;

    /**
     * Find all records (limit by fist and max parameters)
     *
     * @param first - the start position of the first result, numbered from 0
     * @param max   - maximum number of results to retrieve
     * @return List<T>
     * @throws FinderException
     */
    public <T> List<T> findAll(Class<T> clazz, int first, int max, String orderBy, String orderDirection) throws FinderException;

    /**
     * Find all records by list of criteria
     *
     * @param criteria - list of criteria
     * @return List<T>
     * @throws FinderException
     */
    public <T> List<T> getByCriteria(Class<T> clazz, Hashtable<String, String> criteria, int first, int max, String orderBy, String orderDirection) throws FinderException;

    /**
     * Count all records
     *
     * @return count of records
     * @throws
     */
    public <T> Long count(Class<T> clazz);


    public <T> T SQLQuery(String sql) throws FinderException;

    public <T> List<T> SQLNativeQuery(String sql) throws FinderException;

    public <T> List<T> SQL(String sql) throws FinderException;

}
