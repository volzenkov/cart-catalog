package com.aqua.dao;

import com.aqua.domain.AttributeDef;

import java.util.List;

public interface AttributeDefDAO extends GenericDAO<AttributeDef>{

	public void addAttributeDef(AttributeDef attributeDef);

    public void updateAttributeDef(AttributeDef attributeDef);

	public List<AttributeDef> listAttributeDefs();

    public void removeAttributeDef(Long id);
}