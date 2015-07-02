package com.aqua.services;

import com.aqua.dao.GenericDAO;
import com.aqua.dao.exceptions.CreateException;
import com.aqua.dao.exceptions.FinderException;
import com.aqua.domain.AttributeDef;
import com.aqua.domain.AttributeGroup;
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
public class AttributeDefService {

    @Autowired
    private GenericDAO genericDAO;

    public void save(AttributeDef attributeDef) {
        try {
            genericDAO.create(attributeDef);
        } catch (CreateException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AttributeDef> listAttributeDefs() {
        try {
            return genericDAO.findAll(AttributeDef.class);
        } catch (FinderException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setGenericDAO(GenericDAO genericDAO) {
        this.genericDAO = genericDAO;
    }
}
