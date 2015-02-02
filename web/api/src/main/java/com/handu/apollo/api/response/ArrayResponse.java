package com.handu.apollo.api.response;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by markerking on 14/11/3.
 */
public class ArrayResponse<T extends ResponseObject> implements ResponseObject {
    private transient List<T> list;
    public ArrayResponse() {
        this.list = Lists.newArrayList();
    }

    public ArrayResponse(List<T> responses) {
        this.setResponses(responses);
    }

    public void setResponses(List<T> responses) {
        this.list = responses;
    }

    public List<T> getResponses() {
        return this.list;
    }

    public void add(T t) {
        this.list.add(t);
    }

    public void add(int index, T t) {
        this.list.add(index, t);
    }
}
