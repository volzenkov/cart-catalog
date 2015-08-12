package com.aqua.dao.legacy;

import com.aqua.domain.AttributeDef;
import com.aqua.domain.Category;
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
public class AttributeDefExtractor extends LegacyDatabaseExtractor {

    @Test
    @Rollback(false)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void extractCategories() throws Exception {

        final Map<Long, Long> idsMap = new HashMap<>();

        handleStatement(new DbStatementWorker() {

            @Override
            public void doWork(Statement statement) throws SQLException {
                String selectTableSQL =
                        "select * from oc_attribute as a join oc_attribute_description as ad on a.attribute_id = ad.attribute_id;";

                System.out.println(selectTableSQL);

                ResultSet rs = statement.executeQuery(selectTableSQL);

                while (rs.next()) {

                    long legacyAttributeId = rs.getInt("attribute_id");

                    AttributeDef attributeDef = new AttributeDef();
                    attributeDef.setName(rs.getString("name"));

                    commonDAO.create(attributeDef);

                    idsMap.put(legacyAttributeId, attributeDef.getId());
                }
            }

            @Override
            public void handleError(Exception e) {
                System.out.println(e.getMessage());
            }
        });

    }
}
