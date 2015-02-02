package com.handu.apollo.rule;

import lombok.Data;

/**
 * Created by markerking on 14-6-12.
 */
@Data
public class RuleInput {
    private Long id;
    private Long definitionId;
    private Long definitionVersionId;
    private String name;
}
