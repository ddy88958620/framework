package com.handu.apollo.rule;

import lombok.Data;

import java.util.Date;

/**
 * Created by markerking on 14-6-12.
 */
@Data
public class RuleSnippet {
    private Long id;
    private Long definitionId;
    private Long definitionVersionId;
    private String snippet;
    private Integer weight;
    private Date effective;
    private Date expires;
}
