package com.handu.apollo.rule;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * Created by markerking on 14-6-12.
 */
@Data
public class RuleExecutionHistory {
    private Long definitionId;
    private Long definitionVersionId;
    private Date executionTime;
    private String inputBefore;
    private String inputAfter;
    private Map<String, Object> outputs;
}
