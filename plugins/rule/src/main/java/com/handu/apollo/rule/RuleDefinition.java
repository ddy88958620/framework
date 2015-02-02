package com.handu.apollo.rule;

import com.google.common.collect.Lists;
import com.handu.apollo.utils.Log;
import com.handu.apollo.utils.exception.ApolloRuntimeException;
import lombok.Data;

import java.util.List;

/**
 * Created by markerking on 14-6-12.
 */
@Data
public class RuleDefinition {
    private static final Log LOG = Log.getLog(RuleDefinition.class);
    private static final String NO_VERSION = "No version found in this rule definition.";
    private static final String NO_USED_VERSION = "No `USED` version found in this rule definition.";
    private static final String NO_VERSION_OF_ID = "No version of [%d] found in this rule definition.";

    private Long id;
    private List<RuleDefinitionVersion> versions;

    /**
     * Add version to versions
     *
     * @param version RuleDefinitionVersion
     * */
    public void addVersion(RuleDefinitionVersion version) {
        if (versions == null) {
            versions = Lists.newArrayList();
        }
        versions.add(version);
    }

    /**
     * Get used version
     *
     * @return RuleDefinitionVersion
     */
    public RuleDefinitionVersion usedVersion() {
        RuleDefinitionVersion version = null;

        if (versions == null || versions.size() == 0) {
            LOG.error(NO_VERSION);
            throw new ApolloRuntimeException(NO_VERSION);
        } else {
            for (RuleDefinitionVersion _version : versions) {
                if (_version.getState() == RuleDefinitionVersion.State.USED) {
                    version = _version;
                    break;
                }
            }
        }

        if (version == null) {
            LOG.error(NO_USED_VERSION);
            throw new ApolloRuntimeException(NO_USED_VERSION);
        }

        return version;
    }

    /**
     * Get last version
     *
     * @return RuleDefinitionVersion
     */
    public RuleDefinitionVersion lastVersion() {
        if (versions == null || versions.size() == 0) {
            LOG.error(NO_VERSION);
            throw new ApolloRuntimeException(NO_VERSION);
        }

        return versions.get(versions.size() - 1);
    }

    /**
     * Get version by version number
     *
     * @return RuleDefinitionVersion
     * */
    public RuleDefinitionVersion version(Integer v) {
        RuleDefinitionVersion version = null;

        if (versions == null || versions.size() == 0) {
            LOG.error(NO_VERSION);
            throw new ApolloRuntimeException(NO_VERSION);
        } else {
            for (RuleDefinitionVersion _version : versions) {
                if (_version.getVersion().equals(v)) {
                    version = _version;
                    break;
                }
            }
        }

        if (version == null) {
            LOG.error(String.format(NO_VERSION_OF_ID, v));
            throw new ApolloRuntimeException(String.format(NO_VERSION_OF_ID, v));
        }

        return version;
    }

    /**
     * Get used-version snippets
     *
     * @return List<RuleSnippet>
     */
    public List<RuleSnippet> usedSnippets() {
        return usedVersion().getSnippets();
    }

    /**
     * Get used-version inputs
     *
     * @return List<RuleInput>
     */
    public List<RuleInput> usedInputs() {
        return usedVersion().getInputs();
    }

    /**
     * Get used-version outputs
     *
     * @return List<RuleOutput>
     */
    public List<RuleOutput> usedOutputs() {
        return usedVersion().getOutputs();
    }
}
