package com.aqua.services;

import com.aqua.dao.GenericDAO;
import com.aqua.dao.exceptions.FinderException;
import com.aqua.domain.Category;
import com.aqua.services.tree.PrintIndentedVisitor;
import com.aqua.services.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private GenericDAO genericDAO;

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
        Tree<Category> rootNode = new Tree<>(new Category("root"));
        Tree<Category> current = rootNode;

//        try {
//            List<Category> categories = genericDAO.findAll(Category.class);
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

            List<Category> categories = Arrays.asList(category1, category2, category3, category4, category5);

            Collections.sort(categories, new Comparator<Category>() {
                @Override
                public int compare(Category o1, Category o2) {
                    if (o1 == o2) return 0;
                    if (o1 == null) return 1;
                    if (o2 == null) return -1;
                    if (o1.getParentNumericPath() == null) return 1;
                    if (o2.getParentNumericPath() == null) return -1;
                    return o1.getParentNumericPath().compareTo(o2.getParentNumericPath());
                }
            });
            Map<String, Category> map = new HashMap<>();
            for (Category category : categories) {
                map.put(String.valueOf(category.getId()), category);
            }

            for (Category tree : categories) {
                Tree<Category> root = current;
                for (String key : tree.getParentNumericPath().split("\\.")) {
                    current = current.child(map.get(key));
                }
                current = root;
            }

            rootNode.accept(new PrintIndentedVisitor(0));
        return rootNode;
//        } catch (FinderException e) {
//            e.printStackTrace();
//        }
    }

    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }
}
