package com.aqua.services;

import com.aqua.dao.exceptions.FinderException;
import com.aqua.domain.AttributeDef;
import com.aqua.domain.Category;
import com.aqua.services.tree.PrintIndentedVisitor;
import com.aqua.services.tree.Tree;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private BaseCRUDHelper baseCRUDHelper;

    public void save(Category category) {
        baseCRUDHelper.saveOrUpdate(category);
    }

    public List<Category> list() {
        return baseCRUDHelper.list(Category.class);
    }

    public List<Category> listChildCategories(long parentId) {
        return baseCRUDHelper.executeNamedQueryWithResult(
                "getChildCategoriesByParentId", new Long[]{parentId});
    }

    public Tree<Category> buildCategoriesTree() {

        try {
            List<Category> categories = baseCRUDHelper.list(Category.class);

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
                for (Category node : buildParentsStack(category, 1)) {
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

    private Deque<Category> buildParentsStack(Category category, long headCategoryId) {
        Deque<Category> parentsStack = new ArrayDeque<>();
        Category parent = category;
        do {
            parentsStack.push(parent);
            parent = parent.getParent();
        } while (parent != null && parent.getId() != headCategoryId);
        return parentsStack;
    }

    private Category retrieveRootParent(Category category, long headCategoryId) {
        Category parent = category;
        while (parent.getId() != headCategoryId) {
            parent = parent.getParent();
        }
        return parent;
    }

    public Tree<Category> buildCategoriesTree(Category parentCategory) {

        try {

            if (parentCategory != null) {
                String idsPathCriteria = parentCategory.getId() + ".%";
                if (parentCategory.getParentNumericPath() != null) {
                    idsPathCriteria = parentCategory.getParentNumericPath() + idsPathCriteria;
                }

                List<Category> categories = baseCRUDHelper.executeNamedQueryWithResult(
                        "getCategoriesByParentId", new String[]{idsPathCriteria});

                Tree<Category> rootNode = new Tree<>(retrieveRootParent(categories.get(0), parentCategory.getId()));
                Tree<Category> current = rootNode;

                for (Category category : categories) {
                    Tree<Category> root = current;
                    for (Category node : buildParentsStack(category, parentCategory.getId())) {
                        current = current.child(node);
                    }
                    current = root;
                }

                rootNode.accept(new PrintIndentedVisitor(0));
                return rootNode;
            } else {
                throw new RuntimeException();
            }
        } catch (FinderException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<String> getConsumersByCategory(Category category) {
        String parentNumericPath = category.getId() + ".%";
        if (category.getParentNumericPath() != null) {
            parentNumericPath = category.getParentNumericPath() + parentNumericPath;
        }

        AttributeDef consumerAttributeDef = baseCRUDHelper.getById(AttributeDef.class, 1L);

        if (consumerAttributeDef != null) {
            return baseCRUDHelper.executeNamedQueryWithResult("getDistinctAttributeValuesByAttributeDefAndCategory",
                    new Object[]{consumerAttributeDef.getId(), parentNumericPath});
        } else {
            return Collections.emptyList();
        }
    }


}
