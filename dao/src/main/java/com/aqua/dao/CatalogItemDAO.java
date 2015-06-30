package com.aqua.dao;

import com.aqua.domain.CatalogItem;

import java.util.List;

public interface CatalogItemDAO extends GenericDAO<CatalogItem>{

	public void addCatalogItem(CatalogItem CatalogItem);

    public void updateCatalogItem(CatalogItem CatalogItem);

	public List<CatalogItem> listCatalogItems();

    public List<CatalogItem> listCatalogItemsByParentId(Long parentId);

    public void removeCatalogItem(Long id);
}