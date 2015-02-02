package com.handu.apollo.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.handu.apollo.core.ApiErrorCode;
import com.handu.apollo.utils.json.JsonUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by markerking on 14-4-3.
 */
public class ApiResponseSerializerTest {

    @Test
    public void TestSerializedJsonResponse() {
        TestJsonResponse tjr = new TestJsonResponse();
        tjr.setJson("{a:10, b:11}");
        String json = ApiResponseSerializer.toSerializedJSON(tjr);

        System.out.println(String.format("序列化后的JSON内容为: %s", json));
    }

    @Test
    public void testToSerializedJSON() throws IOException {
        TestResponse base = new TestResponse();
        base.setId(new Integer(0x563F2F70 << 34).longValue());
        base.setCreated(new Date());
        base.setRemoved(new Date());

        String json = ApiResponseSerializer.toSerializedJSON(base);

        System.out.println(String.format("序列化后的JSON内容为: %s", json));

        JsonNode root = JsonUtil.getMapper().readTree(json);

        assertNotNull(root);
    }

    @Test
    public void testToSerializedJSONWithListResponse() throws IOException {
        List<BaseResponse> responseList = Lists.newArrayList();

        ExceptionResponse er = new ExceptionResponse();
        er.setErrorCode(ApiErrorCode.ACCOUNT_ERROR.getHttpCode());
        er.setErrorText("用户错误");

        ExceptionResponse er2 = new ExceptionResponse();
        er2.setErrorCode(ApiErrorCode.INTERNAL_ERROR.getHttpCode());
        er2.setErrorText("内部错误");

        responseList.add(er);
        responseList.add(er2);

        ListResponse<BaseResponse> listResponse = new ListResponse<BaseResponse>();
        listResponse.setResponses(responseList);

        String json = ApiResponseSerializer.toSerializedJSON(listResponse);

        System.out.println(String.format("序列化后的JSON内容为: %s", json));

        JsonNode root = JsonUtil.getMapper().readTree(json);

        assertNotNull(root);
    }

    @Test
    public void testToSerializedJSONWithArrayResponse() throws IOException {
        List<BaseResponse> responseList = Lists.newArrayList();

        ExceptionResponse er = new ExceptionResponse();
        er.setErrorCode(ApiErrorCode.ACCOUNT_ERROR.getHttpCode());
        er.setErrorText("用户错误");

        ExceptionResponse er2 = new ExceptionResponse();
        er2.setErrorCode(ApiErrorCode.INTERNAL_ERROR.getHttpCode());
        er2.setErrorText("内部错误");

        responseList.add(er);
        responseList.add(er2);

        ArrayResponse<BaseResponse> arrayResponse = new ArrayResponse<BaseResponse>();
        arrayResponse.setResponses(responseList);

        String json = ApiResponseSerializer.toSerializedJSON(arrayResponse);

        System.out.println(String.format("序列化后的JSON内容为: %s", json));

        JsonNode root = JsonUtil.getMapper().readTree(json);

        assertNotNull(root);
    }

    static class TestResponse extends BaseResponse {
        private Long id;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date created;
        private Date modified;
        private Date removed;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public Date getModified() {
            return modified;
        }

        public void setModified(Date modified) {
            this.modified = modified;
        }

        public Date getRemoved() {
            return removed;
        }

        public void setRemoved(Date removed) {
            this.removed = removed;
        }
    }

    static class TestJsonResponse extends JsonResponse {

        private String json;

        @Override
        public String getJson() {
            return json;
        }

        @Override
        public void setJson(String json) {
            this.json = json;
        }
    }

}
