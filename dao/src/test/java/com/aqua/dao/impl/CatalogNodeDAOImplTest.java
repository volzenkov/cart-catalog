package com.aqua.dao.impl;

import com.aqua.dao.*;
import com.aqua.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class CatalogNodeDAOImplTest {

    @Autowired
    private CatalogItemDAO catalogItemDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private AttributeDefDAO attributeDefDAO;

    @Autowired
    private AttributeValueDAO attributeValueDAO;

    @Autowired
    private AttributeGroupDAO attributeGroupDAO;

    @Test
    public void testAddCatalogNode() throws Exception {

        Category category = new Category("category1");

        category.getMeta().add("meta1");
        category.getMeta().add("meta2");
        category.getTags().add("tag1");

        categoryDAO.addCategory(category);

        AttributeDef attributeDef1 = new AttributeDef("length", AttributeType.INT);
        AttributeDef attributeDef2 = new AttributeDef("width", AttributeType.INT);
        AttributeDef attributeDef3 = new AttributeDef("height", AttributeType.INT);

        attributeDefDAO.addAttributeDef(attributeDef1);
        attributeDefDAO.addAttributeDef(attributeDef2);
        attributeDefDAO.addAttributeDef(attributeDef3);

        AttributeGroup attributeGroup = new AttributeGroup("group1");

        attributeGroup.getAttributeDefs().add(attributeDef1);
        attributeGroup.getAttributeDefs().add(attributeDef2);
        attributeGroup.getAttributeDefs().add(attributeDef3);

        attributeGroupDAO.addAttributeGroup(attributeGroup);

        CatalogItem item = new CatalogItem("test-item");
        item.setParent(category);
        item.getAttributeValues().add(new AttributeValue(attributeDef1, item, "l"));
        item.getAttributeValues().add(new AttributeValue(attributeDef2, item, "2"));
        item.getAttributeValues().add(new AttributeValue(attributeDef3, item, "3"));

        catalogItemDAO.addCatalogItem(item);

//        attributeValueDAO.addAttributeValue(new AttributeValue(attributeDef1, item, "l"));
//        attributeValueDAO.addAttributeValue(new AttributeValue(attributeDef2, item, "2"));
//        attributeValueDAO.addAttributeValue(new AttributeValue(attributeDef3, item, "3"));

        catalogItemDAO.listCatalogItems();

    }

    @Test
    public void testAddCategoryTree() throws Exception {

        Category root = new Category("Root");
        categoryDAO.addCategory(root);

        Category category = new Category("qwe", root);
        categoryDAO.addCategory(category);

        Category category1 = new Category("asd", category);
        categoryDAO.addCategory(category1);

        Category category2 = new Category("zxc", root);
        categoryDAO.addCategory(category2);

        Category testCategory = categoryDAO.findByPrimaryKey(category1.getId());
        testCategory.setParent(category2);
        categoryDAO.updateCategory(testCategory);
    }
}