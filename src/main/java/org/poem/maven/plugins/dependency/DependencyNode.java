package org.poem.maven.plugins.dependency;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Exclusion;
import org.poem.maven.plugins.dependency.traversal.DependencyNodeVisitor;

import java.util.List;

/**
 * Represents an artifact node within a Maven project's dependency graph. Notice there is no support for omitted nodes
 * at the moment, only dependencies kept in the resolved dependency list are available.
 *
 * @author poem
 */
public interface DependencyNode {

    /**
     * Artifact for this DependencyNode.
     *
     * @return Artifact for this DependencyNode.
     */
    Artifact getArtifact();

    /**
     * 获取子节点
     *
     * @return children of this DependencyNode.
     */
    List<DependencyNode> getChildren();

    /**
     * Applies the specified dependency node visitor to this dependency node and its children.
     *
     * @param visitor the dependency node visitor to use
     * @return the visitor result of ending the visit to this node
     * @since 1.1
     */
    boolean accept(DependencyNodeVisitor visitor);

    /**
     * Gets the parent dependency node of this dependency node.
     *
     * @return the parent dependency node
     */
    DependencyNode getParent();

    /**
     * Gets the version or version range for the dependency before dependency management was applied (if any).
     *
     * @return The dependency version before dependency management or {@code null} if the version was not managed.
     */
    String getPremanagedVersion();

    /**
     * Gets the scope for the dependency before dependency management was applied (if any).
     *
     * @return The dependency scope before dependency management or {@code null} if the scope was not managed.
     */
    String getPremanagedScope();

    /**
     * A constraint on versions for a dependency. A constraint can either consist of one or more version ranges or a
     * single version.
     *
     * @return The constraint on the dependency.
     */
    String getVersionConstraint();

    /**
     * Returns a string representation of this dependency node.
     *
     * @return the string representation
     */
    String toNodeString();

    /**
     * @return true for an optional dependency.
     */
    Boolean getOptional();

    /**
     * @return the exclusions of the dependency
     */
    List<Exclusion> getExclusions();
}
