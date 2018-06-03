package org.jboss.maven.plugins.enforcer.rules.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractBanDependencies;
import org.apache.maven.plugins.enforcer.utils.ArtifactMatcher;

/**
 * This rule banned all dependencies that its version matches the specified <code>versionPattern</code>
 * unless it is added to <code>includes</code> explicitly.
 *
 * @author <a href="mailto:lgao@redhat.com">Lin Gao</a>
 */
public class BanVersionDependenciesRule extends AbstractBanDependencies {

    /**
     * The pattern which is used to match the dependency version
     */
    private String versionPattern;

    private static final List<String> DEFAULT_SCOPES = new ArrayList<String>();
    static {
        DEFAULT_SCOPES.add("test");
        DEFAULT_SCOPES.add("system");
    }

    // TOOD: ignore the artifact in whole?
    /**
     * Dependencies that are in the ignored scopes will be ignored, which means they won't be banned.
     * Default to 'test' and 'system'
     */
    private List<String> ignoreScopes = DEFAULT_SCOPES;

    /**
     * Whether this rule is enabled or not. Default to yes.
     */
    private boolean enabled = true;

    /**
     * Whether ignore dependencies which are declared as optional. Default to yes.
     */
    private boolean ignoreOptional = true;

    /**
     * Specify the allowed dependencies. This can be a list of artifacts in the format
     * <code>groupId[:artifactId][:version]</code>. Any of the sections can be a wildcard 
     * by using '*' (ie group:*:1.0) <br>
     * Includes override the exclude rules. It is meant to allow wide exclusion rules 
     * with wildcards and still allow a
     * smaller set of includes. <br>
     * For example, to ban all xerces except xerces-api -> exclude "xerces", include "xerces:xerces-api"
     * 
     * @see {@link #setIncludes(List)}
     * @see {@link #getIncludes()}
     */
    private List<String> includes;

    @Override
    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        if (!enabled)
            return;
        super.execute(helper);
    }

    @Override
    protected Set<Artifact> checkDependencies(final Set<Artifact> dependencies, final Log log) throws EnforcerRuleException {
        if (dependencies == null || dependencies.isEmpty()) {
            log.info("No dependnecies to check.");
            return Collections.emptySet();
        }
        if (this.versionPattern == null || this.versionPattern.trim().length() == 0) {
            log.warn("'versionPattern' is not defined, no dependencies will be banned.");
            return Collections.emptySet();
        }
        Pattern pattern = Pattern.compile(versionPattern);
        Set<Artifact> excluded = null;
        for (Artifact dep : dependencies) {
            if (!ignored(dep) && !includeExplicitly(dep)) {
                String version = dep.getVersion();
                if (pattern.matcher(version).matches()) {
                    if (excluded == null) {
                        excluded = new HashSet<Artifact>();
                    }
                    excluded.add(dep);
                }
            }
        }
        return excluded == null ? Collections.emptySet() : excluded;
    }

    private boolean ignored(Artifact dep) {
        if (ignoreOptional && dep.isOptional()) {
            return true;
        }
        String scope = dep.getScope();
        if (scope == null) {
            scope = "compile";
        }
        if (ignoreScopes != null && !ignoreScopes.isEmpty()) {
            for (String s : this.ignoreScopes) {
                if (s != null && s.trim().length() > 0) {
                    if (s.equalsIgnoreCase(scope)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // check whether the dependency is defined included explicitly
    private boolean includeExplicitly(final Artifact dep) throws EnforcerRuleException {
        if (this.includes == null || this.includes.isEmpty()) {
            return false;
        }
        for (String pattern : includes) {
            if (pattern != null && pattern.trim().length() > 0) {
                String[] subStrings = pattern.split(":");
                subStrings = StringUtils.stripAll(subStrings);
                String resultPattern = StringUtils.join(subStrings, ":"); // remove all while-spaces in each section.
                ArtifactMatcher.Pattern am = new ArtifactMatcher.Pattern(resultPattern);
                try {
                    if (am.match(dep)) {
                        return true;
                    }
                } catch (InvalidVersionSpecificationException e) {
                    throw new EnforcerRuleException("Invalid Version Range: ", e);
                }
            }
        }
        return false;
    }

    public String getVersionPattern() {
        return versionPattern;
    }

    public void setVersionPattern(String versionPattern) {
        this.versionPattern = versionPattern;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getIgnoreScopes() {
        return ignoreScopes;
    }

    public void setIgnoreScopes(List<String> ignoreScopes) {
        this.ignoreScopes = ignoreScopes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIgnoreOptional() {
        return ignoreOptional;
    }

    public void setIgnoreOptional(boolean ignoreOptional) {
        this.ignoreOptional = ignoreOptional;
    }

}
