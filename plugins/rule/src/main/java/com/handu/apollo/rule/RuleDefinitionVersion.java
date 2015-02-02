package com.handu.apollo.rule;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * Created by markerking on 14-6-12.
 */
@Data
public class RuleDefinitionVersion {
    public enum State {
        USED, UNUSED
    }

    private Long id;
    private Long definitionId;
    private Integer version;
    private State state;

    private List<RuleInput> inputs;
    private List<RuleOutput> outputs;
    private List<RuleSnippet> snippets;

    public void addInput(RuleInput input) {
        if (inputs == null) {
            inputs = Lists.newArrayList();
        }
        inputs.add(input);
    }

    public void addOutput(RuleOutput output) {
        if (outputs == null) {
            outputs = Lists.newArrayList();
        }
        outputs.add(output);
    }

    public void addSnippet(RuleSnippet snippet) {
        if (snippets == null) {
            snippets = Lists.newArrayList();
        }
        snippets.add(snippet);
    }
}
