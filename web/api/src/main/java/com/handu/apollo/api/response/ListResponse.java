package com.handu.apollo.api.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by markerking on 14-4-3.
 */
public class ListResponse<T extends ResponseObject> extends BaseResponse {
    private transient List<T> responses;
    private transient Integer count;

    public ListResponse() {
        this.responses = Lists.newArrayList();
    }

    public ListResponse(List<T> responses) {
        this.setResponses(responses);
    }

    public ListResponse(List<T> responses, Integer count) {
        this.setResponses(responses, count);
    }

    @JsonGetter("list")
    public List<T> getResponses() {
        return responses;
    }

    public void setResponses(List<T> responses) {
        this.responses = responses;
    }

    public void setResponses(List<T> responses, Integer count) {
        this.responses = responses;
        this.count = count;
    }

    public void add(T t) {
        this.responses.add(t);
    }

    public void add(int index, T t) {
        this.responses.add(index, t);
    }

    public Integer getCount() {
        if (count != null) {
            return count;
        }

        if (responses != null) {
            return responses.size();
        }

        return null;
    }
}