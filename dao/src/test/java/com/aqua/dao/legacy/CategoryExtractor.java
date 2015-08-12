package com.aqua.dao.legacy;


import com.aqua.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class CategoryExtractor extends LegacyDatabaseExtractor {

    @Test
    @Rollback(false)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void extractCategories() throws Exception {

        final Map<Long, Category> categoryIdsMap = new HashMap<>();

        handleStatement(new LegacyDatabaseExtractor.DbStatementWorker() {

            @Override
            public void doWork(Statement statement) throws SQLException {
                String selectTableSQL =
                        "select * from oc_category as ca join oc_category_description as cd " +
                        "on cd.category_id = ca.category_id ORDER BY parent_id;";

                System.out.println(selectTableSQL);

                ResultSet rs = statement.executeQuery(selectTableSQL);

                Category rootCategory = commonDAO.findByPrimaryKey(Category.class, 1L);

                while (rs.next()) {

                    long legacyCategoryId = rs.getInt("category_id");
                    long legacyParentId = rs.getInt("parent_id");

                    Category parent = categoryIdsMap.get(legacyParentId);

                    Category category = new Category();
                    category.setName(rs.getString("name"));
                    if (parent != null) {
                        category.setParent(parent);
                    } else {
                        category.setParent(rootCategory);
                    }

                    commonDAO.create(category);

                    categoryIdsMap.put(legacyCategoryId, category);
                }
            }

            @Override
            public void handleError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

        final AttributeDef manufacturerAttributeDef = new AttributeDef();
        manufacturerAttributeDef.setName("Производитель");
        manufacturerAttributeDef.setType(AttributeType.STRING);
        commonDAO.create(manufacturerAttributeDef);

        final Map<Long, AttributeDef> attributeIdsMap = new HashMap<>();
        handleStatement(new LegacyDatabaseExtractor.DbStatementWorker() {

            @Override
            public void doWork(Statement statement) throws SQLException {
                String selectTableSQL =
                        "select * from oc_attribute as a join oc_attribute_description as ad on a.attribute_id = ad.attribute_id";

                System.out.println(selectTableSQL);

                ResultSet rs = statement.executeQuery(selectTableSQL);

                while (rs.next()) {

                    long legacyAttributeId = rs.getInt("attribute_id");

                    AttributeDef attributeDef = new AttributeDef();
                    attributeDef.setName(rs.getString("name"));
                    attributeDef.setType(AttributeType.STRING);

                    commonDAO.create(attributeDef);

                    attributeIdsMap.put(legacyAttributeId, attributeDef);
                }
            }

            @Override
            public void handleError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

        final Map<Long, CatalogItem> productIdsMap = new HashMap<>();
        handleStatement(new LegacyDatabaseExtractor.DbStatementWorker() {

            @Override
            public void doWork(Statement statement) throws SQLException {
                String selectTableSQL =
                        "select p.product_id, model, pd.name, pd.description, length, width, height, weight, base_price, " +
                                "base_currency_code, cost, quantity, location, stock_status_id, category_id, pm.name as manufacturer " +
                                "from oc_product as p " +
                                "join oc_product_description as pd on p.product_id = pd.product_id " +
                                "join oc_manufacturer as pm on p.manufacturer_id = pm.manufacturer_id " +
                                "join oc_product_to_category as pc on pc.product_id = p.product_id and pc.main_category = 1;";

                System.out.println(selectTableSQL);

                ResultSet rs = statement.executeQuery(selectTableSQL);

                while (rs.next()) {

                    long legacyProductId = rs.getInt("product_id");

                    CatalogItem catalogItem = new CatalogItem();
                    catalogItem.setModel(rs.getString("model"));
                    catalogItem.setName(rs.getString("name"));
                    catalogItem.setDescription(rs.getString("description"));

                    catalogItem.setDimensions(new Dimensions(rs.getDouble("length"), rs.getDouble("width"), rs.getDouble("height"), rs.getDouble("weight")));

                    catalogItem.setPrice(new Price(rs.getDouble("base_price"),rs.getString("base_currency_code"),rs.getDouble("cost")));

                    catalogItem.setStock(new Stock(rs.getInt("quantity"), 1, rs.getString("location"), rs.getString("stock_status_id")));

                    long legacyCategoryId = rs.getInt("category_id");
                    catalogItem.setParent(categoryIdsMap.get(legacyCategoryId));

                    commonDAO.create(catalogItem);
                    productIdsMap.put(legacyProductId, catalogItem);

                    String manufacturer = rs.getString("manufacturer");
                    if (manufacturer != null && manufacturer.length() > 0) {
                        AttributeValue attributeValue = new AttributeValue();
                        attributeValue.setCatalogItem(productIdsMap.get(legacyProductId));
                        attributeValue.setAttributeDef(manufacturerAttributeDef);
                        attributeValue.setValue(manufacturer);
                        commonDAO.create(attributeValue);
                    }
                }
            }

            @Override
            public void handleError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

        handleStatement(new LegacyDatabaseExtractor.DbStatementWorker() {

            @Override
            public void doWork(Statement statement) throws SQLException {
                String selectTableSQL =
                        "select * from oc_product_attribute ORDER BY product_id;";

                System.out.println(selectTableSQL);

                ResultSet rs = statement.executeQuery(selectTableSQL);

                while (rs.next()) {

                    long legacyAttributeId = rs.getInt("attribute_id");
                    long legacyProductId = rs.getInt("product_id");

                    if (attributeIdsMap.containsKey(legacyAttributeId) && productIdsMap.containsKey(legacyProductId)) {

                        AttributeValue attributeValue = new AttributeValue();
                        attributeValue.setCatalogItem(productIdsMap.get(legacyProductId));
                        attributeValue.setAttributeDef(attributeIdsMap.get(legacyAttributeId));

                        attributeValue.setValue(rs.getString("text"));

                        commonDAO.create(attributeValue);
                    }

                }
            }

            @Override
            public void handleError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }
}
