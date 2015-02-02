package com.handu.apollo.base;

import lombok.Data;

import java.util.List;

/**
 * Created by markerking on 14/9/3.
 */
@Data
public abstract class TreeVo implements Treeable<Long> {
    private Long id;
    private Long parentId;
    private Boolean leaf;
    private String code;
    private List<Treeable<Long>> children;
}
