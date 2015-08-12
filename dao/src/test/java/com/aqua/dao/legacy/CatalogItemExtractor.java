package com.aqua.dao.legacy;

import com.aqua.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class CatalogItemExtractor extends LegacyDatabaseExtractor {

    @Test
    @Rollback(false)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void extractCategories() throws Exception {

        final Map<Long, Long> productIdsMap = new HashMap<>();

        handleStatement(new LegacyDatabaseExtractor.DbStatementWorker() {

            @Override
            public void doWork(Statement statement) throws SQLException {
                String selectTableSQL =
                        "select * from oc_product as p join oc_product_description as pd on p.product_id = pd.product_id;";

                System.out.println(selectTableSQL);

                ResultSet rs = statement.executeQuery(selectTableSQL);

                while (rs.next()) {

                    long legacyCategoryId = rs.getInt("product_id");

                    CatalogItem catalogItem = new CatalogItem();
                    catalogItem.setModel(rs.getString("model"));
                    catalogItem.setName(rs.getString("name"));
                    catalogItem.setDescription(rs.getString("description"));

                    catalogItem.setDimensions(new Dimensions(rs.getDouble("length"), rs.getDouble("width"), rs.getDouble("height"), rs.getDouble("weight")));

                    catalogItem.setPrice(new Price(rs.getDouble("base_price"),rs.getString("base_currency_code"),rs.getDouble("cost")));

                    catalogItem.setStock(new Stock(rs.getInt("quantity"), 1, rs.getString("location"), rs.getString("stock_status_id")));
                    commonDAO.create(catalogItem);

                    productIdsMap.put(legacyCategoryId, catalogItem.getId());
                }
            }

            @Override
            public void handleError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }
}
