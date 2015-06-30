package com.aqua.dao;

import com.aqua.domain.AttributeGroup;
import com.aqua.domain.Category;

import java.util.List;

public interface AttributeGroupDAO extends GenericDAO<AttributeGroup>{

	public void addAttributeGroup(AttributeGroup attributeGroup);

    public void updateAttributeGroup(AttributeGroup attributeGroup);

	public List<AttributeGroup> listAttributeGroups();

    public void removeAttributeGroup(Long id);
}