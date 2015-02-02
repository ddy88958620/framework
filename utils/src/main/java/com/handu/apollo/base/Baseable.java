package com.handu.apollo.base;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wangfei on 2014/8/14.
 */
public interface Baseable<ID extends Serializable> extends Serializable {

    public ID getId();

    public void setId(ID id);

    public Date getCreated();

    public void setCreated(Date created);

    public Long getCreater();

    public void setCreater(Long creater);

    public Date getModified();

    public void setModified(Date modified);

    public Long getModifier();

    public void setModifier(Long modifier);

    public Date getRemoved();

    public void setRemoved(Date removed);

    public Long getRemover();

    public void setRemover(Long remover);
}
