package com.aqua.dao.impl;

import com.aqua.dao.CommonDAO;
import com.aqua.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-context.xml")
public class CatalogNodeDAOImplTest {

    @Autowired
    private CommonDAO commonDAO;

    @Test
    public void testAddCatalogNode() throws Exception {

        Category category = new Category("category1");

        category.getMeta().add("meta1");
        category.getMeta().add("meta2");
        category.getTags().add("tag1");

        commonDAO.create(category);

        AttributeDef attributeDef1 = new AttributeDef("length", AttributeType.INT);
        AttributeDef attributeDef2 = new AttributeDef("width", AttributeType.INT);
        AttributeDef attributeDef3 = new AttributeDef("height", AttributeType.INT);

        commonDAO.create(attributeDef1);
        commonDAO.create(attributeDef2);
        commonDAO.create(attributeDef3);

        AttributeGroup attributeGroup = new AttributeGroup("group1");

        attributeGroup.getAttributeDefs().add(attributeDef1);
        attributeGroup.getAttributeDefs().add(attributeDef2);
        attributeGroup.getAttributeDefs().add(attributeDef3);

        commonDAO.create(attributeGroup);

        CatalogItem item = new CatalogItem("test-item");
        item.setParent(category);
        item.getAttributeValues().add(new AttributeValue(attributeDef1, item, "l"));
        item.getAttributeValues().add(new AttributeValue(attributeDef2, item, "2"));
        item.getAttributeValues().add(new AttributeValue(attributeDef3, item, "3"));

        commonDAO.create(item);

//        attributeValueDAO.addAttributeValue(new AttributeValue(attributeDef1, item, "l"));
//        attributeValueDAO.addAttributeValue(new AttributeValue(attributeDef2, item, "2"));
//        attributeValueDAO.addAttributeValue(new AttributeValue(attributeDef3, item, "3"));

    }

    @Test
    public void testAddCategoryTree() throws Exception {

        Category root = new Category("Root");
        commonDAO.create(root);

        Category category = new Category("qwe", root);
        commonDAO.create(category);

        Category category1 = new Category("asd", category);
        commonDAO.create(category1);

        Category category2 = new Category("zxc", root);
        commonDAO.create(category2);

        Category testCategory = commonDAO.findByPrimaryKey(Category.class, category1.getId());
        testCategory.setParent(category2);
        commonDAO.update(testCategory);
    }

    @Test
    public void testGetCategories() throws Exception {
        List<CatalogItem> all = commonDAO.findAll(CatalogItem.class);
        for (CatalogItem catalogItem : all) {
            System.out.println(catalogItem);
        }

    }


}