package org.poem.maven.plugins.dependency;

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.poem.maven.plugins.exception.DependencyGraphBuilderException;

import java.util.Collection;

/**
 * Maven project dependency graph builder API, neutral against Maven 2 or Maven 3.
 * @author eventec
 */
public interface DependencyGraphBuilder {
    /**
     * Build the dependency graph.
     *
     * @param buildingRequest the buildingRequest
     * @param filter          artifact filter (can be <code>null</code>)
     * @return the dependency graph
     * @throws DependencyGraphBuilderException if some of the dependencies could not be resolved.
     */
    DependencyNode buildDependencyGraph(ProjectBuildingRequest buildingRequest, ArtifactFilter filter)
            throws DependencyGraphBuilderException;

    /**
     * Build the dependency graph.
     * @param buildingRequest the buildingRequest
     * @param filter          artifact filter (can be <code>null</code>)
     * @param reactorProjects ignored
     * @return the dependency graph
     * @throws DependencyGraphBuilderException if some of the dependencies could not be resolved.
     * @deprecated Use {@link #buildDependencyGraph(ProjectBuildingRequest, ArtifactFilter)} instead
     */
    @Deprecated
    default DependencyNode buildDependencyGraph(
            ProjectBuildingRequest buildingRequest, ArtifactFilter filter, Collection<MavenProject> reactorProjects)
            throws DependencyGraphBuilderException {
        return buildDependencyGraph(buildingRequest, filter);
    }
}