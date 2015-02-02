package com.handu.apollo.api.response;

import com.handu.apollo.api.Output;
import lombok.Data;

import java.util.Date;

/**
 * Created by markerking on 14-4-3.
 */
@Data
public abstract class BaseResponse implements ResponseObject {

    @Output(description="ID")
    private Long id;

    @Output(description="创建时间")
    private Date created;

    @Output(description="创建人")
    private Long creater;

    @Output(description="最后修改时间")
    private Date modified;

    @Output(description="最后修改人")
    private Long modifier;

    @Output(description="删除时间")
    private Date removed;

    @Output(description="删除人")
    private Long remover;
}
