package com.aqua.services;

import com.aqua.dao.GenericDAO;
import com.aqua.dao.exceptions.CreateException;
import com.aqua.dao.exceptions.FinderException;
import com.aqua.domain.Category;
import com.aqua.services.tree.PrintIndentedVisitor;
import com.aqua.services.tree.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private GenericDAO genericDAO;

    public void save(Category category) {
        try {
            genericDAO.create(category);
        } catch (CreateException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Category> listCategoriesByParent(Long parentId) {
        try {
            if (parentId != null) {
                return genericDAO.executeNamedQueryWithResult("getCategoriesByParentId", new Object[]{parentId});
            } else {
                return genericDAO.executeNamedQueryWithResult("getRootCategoriesItems", null);
            }

        } catch (FinderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Tree<Category> buildCategoriesTree() {

        try {
            List<Category> categories = genericDAO.findAll(Category.class);

            Category rootCategory = (Category) CollectionUtils.find(categories, new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    return ((Category) object).getParent() == null;
                }
            });

            categories.remove(rootCategory);

            Tree<Category> rootNode = new Tree<>(rootCategory);
            Tree<Category> current = rootNode;

            for (Category category : categories) {
                Tree<Category> root = current;
                for (Category node : buildParentsStack(category)) {
                    current = current.child(node);
                }
                current = root;
            }

            rootNode.accept(new PrintIndentedVisitor(0));
            return rootNode;
        } catch (FinderException e) {
            throw new RuntimeException(e);
        }
    }

    private Deque<Category> buildParentsStack(Category category) {
        Deque<Category> parentsStack = new ArrayDeque<>();
        Category parent = category;
        while (parent != null) {
            parentsStack.push(parent);
            parent = parent.getParent();
        }
        parentsStack.poll();
        return parentsStack;
    }


    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }

    private List<Category> dummyData() {
        Category category1 = new Category("x1");
        category1.setId(1L);
        category1.setParentNumericPath("1");
        Category category2 = new Category("x2", category1);
        category2.setId(2L);
        category2.setParentNumericPath("1.2");
        Category category3 = new Category("x3", category2);
        category3.setId(3L);
        category3.setParentNumericPath("1.2.3");
        Category category4 = new Category("x4", category1);
        category4.setId(4L);
        category4.setParentNumericPath("1.4");
        Category category5 = new Category("x5", category4);
        category5.setId(5L);
        category5.setParentNumericPath("1.4.5");

        return Arrays.asList(category1, category2, category3, category4, category5);
  }
}
