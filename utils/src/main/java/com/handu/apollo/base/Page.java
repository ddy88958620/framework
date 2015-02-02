package com.handu.apollo.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangfei on 2014/8/13.
 */
public class Page<T> implements Serializable {
    //页码
    @JsonIgnore
    private Integer page;

    //每页显示数量，最大100
    @JsonIgnore
    private Integer pagesize;

    //排序字段
    @JsonIgnore
    private List<String> order;

    private List<T> list;
    private Integer count;

    @JsonIgnore
    public int getStartIndex() {
        int startIndex = 0;
        int pageSizeVal = getPagesize();

        if (pageSizeVal == 0) {
            startIndex = 0;
        } else if (page != null) {
            int pageNum = page;
            if (pageNum > 0) {
                startIndex = pageSizeVal * (pageNum - 1);
            }
        }
        return startIndex;
    }


    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setList(List<T> responses, Integer count) {
        this.list = responses;
        this.count = count;
    }


    public Integer getCount() {
        if (count != null) {
            return count;
        }

        if (list != null) {
            return list.size();
        }

        return null;
    }
}
