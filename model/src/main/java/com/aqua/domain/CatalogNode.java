package com.aqua.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class CatalogNode extends NamedEntity {

    @Column
    private String seo;

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> meta = new ArrayList<>();

    @Column
    @ElementCollection(targetClass=String.class)
    private List<String> tags = new ArrayList<>();

    @OneToOne
    private Category parent;

    @Column
    private boolean enabled;

    public CatalogNode() {}

    public CatalogNode(String name) {
        super(name);
    }

    public CatalogNode(String name, Category parentCategory) {
        super(name);
        this.parent = parentCategory;
    }

    public CatalogNode(String name, String description) {
        super(name, description);
    }


    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public List<String> getMeta() {
        return meta;
    }

    public void setMeta(List<String> meta) {
        this.meta = meta;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
