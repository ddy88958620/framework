package com.handu.apollo.base;

import java.io.Serializable;
import java.util.List;

/**
 * Created by markerking on 14/9/4.
 */
public interface Treeable<ID extends Serializable> extends Serializable {
    public void setId(ID id);
    public ID getId();
    public void setParentId(ID parentId);
    public ID getParentId();
    public void setLeaf(Boolean leaf);
    public Boolean getLeaf();
    public void setCode(String code);
    public String getCode();
    public void setChildren(List<Treeable<ID>> child);
    public List<Treeable<ID>> getChildren();
    public void setText(String text);
    public String getText();
}
