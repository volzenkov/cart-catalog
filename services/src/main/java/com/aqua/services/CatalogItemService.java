package com.aqua.services;

import com.aqua.domain.CatalogItem;
import com.aqua.domain.Category;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.*;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CatalogItemService {

    @Autowired
    private BaseCRUDHelper baseCRUDHelper;

    @Autowired
    private SessionFactory sessionFactory;

    public void save(CatalogItem catalogItem) {
        baseCRUDHelper.saveOrUpdate(catalogItem);
    }

    public List<CatalogItem> list() {
        return baseCRUDHelper.list(CatalogItem.class);
    }

    public void setBaseCRUDHelper(BaseCRUDHelper baseCRUDHelper) {
        this.baseCRUDHelper = baseCRUDHelper;
    }

    @Transactional(readOnly = true)
    public List<CatalogItem> listByFilters(List<CatalogItemFilter> catalogItemFilters) {
        return listByFilters(baseCRUDHelper.getById(Category.class, 1L), catalogItemFilters);
    }

    @Transactional(readOnly = true)
    public List<CatalogItem> listByFilters(Category parentCategory, List<CatalogItemFilter> catalogItemFilters) {

        Set<Long> catalogItemIds = null;
        if (catalogItemFilters != null) {
            List<CatalogItemFilter> filters = new LinkedList<>();
            for (CatalogItemFilter catalogItemFilter : catalogItemFilters) {
                if (catalogItemFilter.getSelectedValues().length > 0 &&
                        catalogItemFilter.getSelectedValues().length < catalogItemFilter.getValues().length) {
                    filters.add(catalogItemFilter);
                }
            }

            catalogItemIds = getByNamedQuery(filters);
        }

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CatalogItem.class);

        if (CollectionUtils.isNotEmpty(catalogItemIds)) {
            criteria.add(Restrictions.in("id", catalogItemIds));
        }

        String parentIdsPath = parentCategory.getId() + ".%";
        if (parentCategory.getParentNumericPath() != null) {
            parentIdsPath = parentCategory.getParentNumericPath() + parentIdsPath;
        }
        criteria.add(Restrictions.like("parentNumericPath", parentIdsPath));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return (List<CatalogItem>) criteria.list();

    }

    private List<CatalogItem> getByHibernateCriteria(List<CatalogItemFilter> catalogItemFilters) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CatalogItem.class);
        criteria.createAlias("attributeValues", "ciav");
        criteria.createAlias("ciav.attributeDef", "ciad");

        for (CatalogItemFilter catalogItemFilter : catalogItemFilters) {
            if (catalogItemFilter.getSelectedValues().length > 0) {

                criteria.add(Restrictions.eq("ciad.id", catalogItemFilter.getAttributeDef().getId()));
                switch (catalogItemFilter.getAttributeDef().getType()) {
                    case STRING:
                    case INT:
                    case DOUBLE: {
                        criteria.add(Restrictions.in("ciav.value", catalogItemFilter.getSelectedValues()));
                        break;
                    }
                    case NUMERIC_RANGE: {
                        criteria.add(Restrictions.between("ciav.value", catalogItemFilter.getSelectedValues()[0], catalogItemFilter.getSelectedValues()[1]));
                        break;
                    }
                }
            }

        }

        return (List<CatalogItem>) criteria.list();
    }

    private List<CatalogItem> getBySqlQuery(List<CatalogItemFilter> catalogItemFilters) {


//        SELECT DISTINCT av.catalog_item_id_fk from `cart-catalog`.attribute_value as av
//        where (av.attributeDef_catalog_item_id = 2 and av.value in ('werwe') )
//           or (av.attributeDef_catalog_item_id = 1 and av.value in ('asdas', '789') );

        StringBuilder sqlQueryBuilder = new StringBuilder("SELECT DISTINCT av.catalog_item_id_fk as ID from attribute_value as av where ");

        if (CollectionUtils.isNotEmpty(catalogItemFilters)) {

            CatalogItemFilter catalogItemFilter;
            int parameterIndex = 0;
            Map<String, Object> queryParams = new HashMap<>();
            Iterator<CatalogItemFilter> iterator = catalogItemFilters.iterator();
            while (iterator.hasNext()) {
                catalogItemFilter = iterator.next();
                parameterIndex++;
                switch (catalogItemFilter.getAttributeDef().getType()) {
                    case STRING:
                    case INT:
                    case DOUBLE: {
                        sqlQueryBuilder.append("(av.attributeDef_id = :ad_id");
                        sqlQueryBuilder.append(parameterIndex);
                        queryParams.put("ad_id" + parameterIndex, catalogItemFilter.getAttributeDef().getId());
                        sqlQueryBuilder.append(" and av.value in (:av_v");
                        sqlQueryBuilder.append(parameterIndex);
                        queryParams.put("av_v" + parameterIndex, catalogItemFilter.getSelectedValues());
                        sqlQueryBuilder.append("))");
                        break;
                    }
//                    case NUMERIC_RANGE: {
//                        criteria.add(Restrictions.between("ciav.value", catalogItemFilter.getSelectedValues()[0], catalogItemFilter.getSelectedValues()[1]));
//                        break;
//                    }
                }
                if (iterator.hasNext()) {
                    sqlQueryBuilder.append(" or ");
                }
            }

            SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sqlQueryBuilder.toString());
            sqlQuery.addScalar("ID", Hibernate.LONG);

            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {

                Object value = entry.getValue();
                if (value instanceof Object[]) {
                    sqlQuery.setParameterList(entry.getKey(), (Object[]) value);
                } else {
                    sqlQuery.setParameter(entry.getKey(), value);
                }
            }

            List<Long> list = sqlQuery.list();
            if (CollectionUtils.isNotEmpty(list)) {
                Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CatalogItem.class);
                criteria.add(Restrictions.in("id", list));
                criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
                return (List<CatalogItem>) criteria.list();
            }
        }
        return Collections.emptyList();
    }

    private Set<Long> getByNamedQuery(List<CatalogItemFilter> catalogItemFilters) {

        if (CollectionUtils.isNotEmpty(catalogItemFilters)) {

            CatalogItemFilter catalogItemFilter;
            Iterator<CatalogItemFilter> iterator = catalogItemFilters.iterator();
            Set<Long> catalogItemIds = null;

            while (iterator.hasNext()) {
                catalogItemFilter = iterator.next();

                Query query = sessionFactory.getCurrentSession().getNamedQuery("getDistinctCatalogItemIdByAttributeValues")
                        .setParameter("attrId", catalogItemFilter.getAttributeDef().getId())
                        .setParameterList("attrValues", catalogItemFilter.getSelectedValues());

                List<Long> list = query.list();
                if (CollectionUtils.isEmpty(list)) {
                    catalogItemIds = Collections.emptySet();
                    break;
                }
                if (catalogItemIds == null) {
                    catalogItemIds = new HashSet<>(list);
                }
                catalogItemIds.retainAll(list);
            }
            return catalogItemIds;
        }
        return Collections.emptySet();
    }
}

//Hibernate: select * from catalog_item this_ left outer join category category4_ on this_.parent_catalog_item_id=category4_.catalog_item_id left outer join category category5_ on category4_.parent_catalog_item_id=category5_.catalog_item_id inner join attribute_value ciav1_ on this_.catalog_item_id=ciav1_.catalog_item_id_fk inner join attribute_def ciad2_ on ciav1_.attributeDef_catalog_item_id=ciad2_.catalog_item_id left outer join catalog_item catalogite8_ on ciav1_.catalog_item_id_fk=catalogite8_.catalog_item_id where ciad2_.catalog_item_id=? and ciav1_.value in (?) and ciad2_.catalog_item_id=? and ciav1_.value in (?)
