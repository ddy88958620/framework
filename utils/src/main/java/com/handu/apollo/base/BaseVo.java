package com.handu.apollo.base;

import lombok.Data;

import java.util.Date;

/**
 * Created by markerking on 14/9/4.
 */
@Data
public class BaseVo implements Baseable<Long> {
    private Long id;
    private Long creater;
    private Date created;
    private Long modifier;
    private Date modified;
    private Long remover;
    private Date removed;
}
