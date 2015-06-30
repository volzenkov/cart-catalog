package com.aqua.dao;

import com.aqua.domain.Category;

import java.util.List;

public interface CategoryDAO extends GenericDAO<Category>{

	public void addCategory(Category category);

    public void updateCategory(Category category);

	public List<Category> listCategoriesByParentId(Long parentId);

    public List<Category> listCategories();

    public void removeCategory(Long id);
}