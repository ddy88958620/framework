package com.handu.apollo.api.response;

/**
 * Created by markerking on 14-4-17.
 */
public abstract class JsonResponse implements ResponseObject {
    public abstract String getJson();
    public abstract void setJson(String json);
}
