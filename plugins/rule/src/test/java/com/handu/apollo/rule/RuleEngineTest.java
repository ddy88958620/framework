package com.handu.apollo.rule;

import com.google.common.collect.Maps;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RuleEngineTest {

    private static final Long DEFINITION_ID = 1L;
    private RuleDefinition definition;

    @Before
    public void setUp() throws Exception {
        definition = new RuleDefinition();
        definition.setId(DEFINITION_ID);
        initVersions();
    }

    private void initVersions() {
        RuleDefinitionVersion version;
        for (int i = 1; i <= 20; i++) {
            version = new RuleDefinitionVersion();
            version.setId(i + 0L);
            version.setDefinitionId(DEFINITION_ID);
            version.setState(RuleDefinitionVersion.State.UNUSED);
            if (i == 20) {
                version.setState(RuleDefinitionVersion.State.USED);
            }
            version.setVersion(i);
            initInputs(version);
            initOutputs(version);
            initSnippets(version);

            definition.addVersion(version);
        }
    }

    private void initInputs(RuleDefinitionVersion version) {
        RuleInput input = new RuleInput();
        input.setName("input");
        input.setDefinitionId(version.getDefinitionId());
        input.setDefinitionVersionId(version.getId());

        version.addInput(input);
    }

    private void initOutputs(RuleDefinitionVersion version) {
        RuleOutput output = new RuleOutput();
        output.setName("output");
        output.setDefinitionId(version.getDefinitionId());
        output.setDefinitionVersionId(version.getId());

        version.addOutput(output);
    }

    private void initSnippets(RuleDefinitionVersion version) {
        RuleSnippet snippet;
        for (int i = 8; i >= 0; i--) {
            snippet = new RuleSnippet();
            snippet.setSnippet("print(\"I'm in version["+version.getId()+"], snippet["+i+"], input[\" + input + \"]\"); output = 100;");
            snippet.setWeight(i);
            snippet.setDefinitionId(version.getDefinitionId());
            snippet.setDefinitionVersionId(version.getId());

            version.addSnippet(snippet);
        }
    }

    @Test
    public void testUsedVersionExec() {
        System.out.println("Start testUsedVersionExec() ========>");
        Map<String, Object> inputs = Maps.newHashMap();
        inputs.put("input", "THIS IS INPUT.");
        RuleExecutionHistory history = RuleEngine.exec(definition.usedVersion(), inputs);

        assertEquals(100, history.getOutputs().get("output"));
    }

    @Test
    public void testLastVersionExec() {
        System.out.println("Start testLastVersionExec() ========>");
        Map<String, Object> inputs = Maps.newHashMap();
        inputs.put("input", "THIS IS INPUT.");
        RuleExecutionHistory history = RuleEngine.exec(definition.version(3), inputs);

        assertEquals(100, history.getOutputs().get("output"));
    }

    @Test
    public void testChangeInputValue() {
        RuleDefinition def = new RuleDefinition();

        RuleDefinitionVersion def_v = new RuleDefinitionVersion();
        def_v.setState(RuleDefinitionVersion.State.USED);

        RuleInput v_input = new RuleInput();
        v_input.setName("user");
        def_v.addInput(v_input);

        RuleOutput v_output = new RuleOutput();
        v_output.setName("copy_user");
        def_v.addOutput(v_output);

        RuleSnippet v_snippet = new RuleSnippet();
        v_snippet.setWeight(1);
        v_snippet.setSnippet("user.name = \"Mac OS X\"; user.age = 40; copy_user = user;");
        def_v.addSnippet(v_snippet);

        def.addVersion(def_v);

        UserMock user = new UserMock();
        user.setName("Marker.King");
        user.setAge(30);

        Map<String, Object> inputs = Maps.newHashMap();
        inputs.put("user", user);

        String inputBefore = inputs.toString();

        RuleExecutionHistory history = RuleEngine.exec(def.usedVersion(), inputs);

        assertEquals("Mac OS X", user.getName());
        assertEquals(user, history.getOutputs().get("copy_user"));
        assertEquals(inputBefore, history.getInputBefore());
        assertNotEquals(inputBefore, history.getInputAfter());
    }

    @Test
    public void testTimeRange() {
        RuleDefinition def = new RuleDefinition();

        RuleDefinitionVersion def_v = new RuleDefinitionVersion();
        def_v.setState(RuleDefinitionVersion.State.USED);

        RuleInput v_input = new RuleInput();
        v_input.setName("user");
        def_v.addInput(v_input);

        RuleOutput v_output = new RuleOutput();
        v_output.setName("copy_user");
        def_v.addOutput(v_output);

        RuleSnippet v_snippet = new RuleSnippet();
        v_snippet.setWeight(1);
        v_snippet.setSnippet("user.name = \"Mac OS X\"; user.age = 40; copy_user = user;");
        def_v.addSnippet(v_snippet);

        RuleSnippet v_snippet2 = new RuleSnippet();
        v_snippet2.setWeight(2);
        v_snippet2.setSnippet("user.name = \"Thinking in Java\";");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis() + 500000);
        v_snippet2.setEffective(c.getTime());
        def_v.addSnippet(v_snippet2);

        RuleSnippet v_snippet3 = new RuleSnippet();
        v_snippet3.setWeight(3);
        v_snippet3.setSnippet("user.name = \"Thinking in Java of 2\";");
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(System.currentTimeMillis() - 50000);
        v_snippet3.setExpires(c2.getTime());
        def_v.addSnippet(v_snippet3);

        RuleSnippet v_snippet4 = new RuleSnippet();
        v_snippet4.setWeight(4);
        v_snippet4.setSnippet("user.name = \"Thinking in Java of 3\";");
        Calendar c3 = Calendar.getInstance();
        c3.setTimeInMillis(System.currentTimeMillis() - 5000);
        v_snippet4.setEffective(c3.getTime());

        Calendar c4 = Calendar.getInstance();
        c4.setTimeInMillis(System.currentTimeMillis() + 5000);
        v_snippet4.setExpires(c4.getTime());
        def_v.addSnippet(v_snippet4);

        def.addVersion(def_v);

        UserMock user = new UserMock();
        user.setName("Marker.King");
        user.setAge(30);

        Map<String, Object> inputs = Maps.newHashMap();
        inputs.put("user", user);

        String inputBefore = inputs.toString();

        RuleExecutionHistory history = RuleEngine.exec(def.usedVersion(), inputs);

        assertEquals("Mac OS X", user.getName());
        assertNotEquals("Thinking in Java of 3", user.getName());
        assertEquals(user, history.getOutputs().get("copy_user"));
        assertEquals(inputBefore, history.getInputBefore());
        assertNotEquals(inputBefore, history.getInputAfter());
    }

    @Data
    public class UserMock {
        private String name;
        private int age;
    }
}