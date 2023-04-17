package org.poem.maven.plugins.dependency;


import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.project.ProjectBuildingRequest;
import org.poem.maven.plugins.exception.DependencyCollectorBuilderException;

/**
 * Maven 项目依赖原始依赖收集器 API，提供针对 Maven 3 和 Maven 3.1+ 的抽象层特定的以太实现。
 * @author poem
 */
public interface DependencyCollectorBuilder {

    /**
     * collect the project's raw dependency graph, with information to allow the API client to reason on its own about
     * dependencies.
     *
     * @param buildingRequest the request with the project to process its dependencies.
     * @param filter an artifact filter if not all dependencies are required (can be <code>null</code>)
     * @return the raw dependency tree
     * @throws DependencyCollectorBuilderException if some of the dependencies could not be collected.
     */
    default DependencyNode collectDependencyGraph(ProjectBuildingRequest buildingRequest, ArtifactFilter filter)
            throws DependencyCollectorBuilderException {
        return collectDependencyGraph(new DependencyCollectorRequest(buildingRequest, filter));
    }

    /**
     * collect the project's raw dependency graph, with information to allow the API client to reason on its own about
     * dependencies.
     *
     * @param dependencyCollectorRequest the request with different paramaters.
     * @return the raw dependency tree
     * @throws DependencyCollectorBuilderException if some of the dependencies could not be collected.
     * @since 3.2.1
     */
    DependencyNode collectDependencyGraph(DependencyCollectorRequest dependencyCollectorRequest)
            throws DependencyCollectorBuilderException;
}