package com.aqua.dao;

import com.aqua.domain.AttributeValue;

import java.util.List;

public interface AttributeValueDAO extends GenericDAO<AttributeValue>{

	public void addAttributeValue(AttributeValue attributeValue);

    public void updateAttributeValue(AttributeValue attributeValue);

	public List<AttributeValue> listAttributeValues();

    public void removeAttributeValue(Long id);
}