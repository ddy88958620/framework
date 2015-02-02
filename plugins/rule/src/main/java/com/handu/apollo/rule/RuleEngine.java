package com.handu.apollo.rule;

import com.google.common.collect.Maps;
import com.handu.apollo.rule.utils.ComparatorWeight;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.*;

/**
 * Created by markerking on 14-6-13.
 */
public class RuleEngine {
    private static final Log LOG = Log.getLog(RuleEngine.class);

    public static RuleExecutionHistory exec(RuleDefinitionVersion version, Map<String, Object> inputs) {
        if (inputs == null) {
            LOG.warn(String.format("Rule definition[%d], version[%d], input is null. Set it to empty.", version.getDefinitionId(), version.getVersion()));
            inputs = Maps.newHashMap();
        }

        RuleExecutionHistory history = new RuleExecutionHistory();
        history.setInputBefore(inputs.toString());

        Map<String, Object> outputs = run(version, inputs);

        history.setInputAfter(inputs.toString());

        history.setDefinitionId(version.getDefinitionId());
        history.setDefinitionVersionId(version.getId());
        history.setExecutionTime(new Date());

        history.setOutputs(outputs);

        return history;
    }

    private static Map<String, Object> run(RuleDefinitionVersion version, Map<String, Object> inputs) {
        try {
            List<RuleInput> inputNames = version.getInputs();

//            Interpreter interpreter = new Interpreter();
            Binding binding = new Binding();
            GroovyShell shell = new GroovyShell(binding);

            //设置输入
            if (inputNames != null) {
                for (RuleInput input : inputNames) {
//                    interpreter.set(input.getName(), inputs.get(input.getName()));
                    binding.setVariable(input.getName(), inputs.get(input.getName()));
                }
            }

            List<RuleSnippet> snippets = version.getSnippets();
            if (snippets == null || snippets.size() == 0) {
                LOG.error("Snippets is null, engine stopped.");
                throw new ApolloRuntimeException("Snippets is null, engine stopped.");
            }
            //权重排序
            Collections.sort(snippets, new ComparatorWeight());

            //执行代码段
            for (RuleSnippet snippet : snippets) {
                long effective = snippet.getEffective() == null ? 0 : snippet.getEffective().getTime();
                long expires = snippet.getExpires() == null ? Long.MAX_VALUE : snippet.getExpires().getTime();
                if (System.currentTimeMillis() >= effective && System.currentTimeMillis() <= expires) {
//                    interpreter.eval(snippet.getSnippet());
                    shell.evaluate(snippet.getSnippet());
                }
            }

            //输出
            Map<String, Object> ruleOutputs = Maps.newHashMap();

            //收集输出
            List<RuleOutput> outputs = version.getOutputs();
            if (outputs != null) {
                for (RuleOutput output : outputs) {
//                    ruleOutputs.put(output.getName(), interpreter.get(output.getName()));
                    ruleOutputs.put(output.getName(), binding.getVariable(output.getName()));
                }
            }

            return ruleOutputs;
//        } catch (EvalError evalError) {
        } catch (Exception evalError) {
            LOG.error("An error occured during Snippets execution.", evalError);
            throw new ApolloRuntimeException("An error occured during Snippets execution.", evalError);
        }
    }
}
