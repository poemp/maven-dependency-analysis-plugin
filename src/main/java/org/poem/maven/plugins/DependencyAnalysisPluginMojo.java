package org.poem.maven.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.poem.maven.plugins.dependency.DependencyCollectorBuilder;
import org.poem.maven.plugins.dependency.DependencyGraphBuilder;
import org.poem.maven.plugins.dependency.DependencyNode;
import org.poem.maven.plugins.dependency.remote.RemoteInfoWriter;
import org.poem.maven.plugins.dependency.traversal.RemoteDependencyNodeVisitor;
import org.poem.maven.plugins.dependency.traversal.SerializingDependencyNodeVisitor;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * Goal which touches a timestamp file.
 *
 * @author poem
 * @goal touch
 * @phase process-sources
 */
@Mojo(name = "analysis", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DependencyAnalysisPluginMojo extends AbstractMojo {

    /**
     * 日志组件
     */
    private final Log log = getLog();


    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * 拦截
     */
    @Parameter
    private ArtifactFilter artifactFilter;

    /**
     * 输出文件目录
     */
    @Parameter
    private File outputFile;

    /**
     * 是不是展示详细
     */
    @Parameter
    private boolean verbose;

    /**
     * 远程分析地址
     */
    @Parameter
    private String analysisUrl;

    /**
     *
     */
    @Component
    private DependencyGraphBuilder graphBuilder;

    /**
     *
     */
    @Component
    private DependencyCollectorBuilder collectorBuilder;

    @Override
    public void execute() throws MojoExecutionException {
        // Code currently assumes project has been set...
        ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());
        buildingRequest.setProject(project);
        log.info("============ hello dependency graph========== ");
        log.info(">>>> session:" + session);
        log.info(">>>> project-name:" + project.getName());
        log.info(">>>> project-version:" + project.getVersion());
        log.info(">>>> project-artifactId:" + project.getArtifactId());
        log.info(">>>> project-group:" + project.getGroupId());
        log.info(">>>> project-name:" + project.getName());
        log.info(">>>> verbose:" + verbose);

        try {
            //依赖节点
            DependencyNode node;
            if (verbose) {
                node = collectorBuilder.collectDependencyGraph(buildingRequest, artifactFilter);
            } else {
                node = graphBuilder.buildDependencyGraph(buildingRequest, artifactFilter);
            }

            if (outputFile != null) {
                log.info(">>>> local outputFile:" + outputFile);
                outputFile.getParentFile().mkdirs();

                try (Writer writer = new FileWriter(outputFile)) {
                    node.accept(new SerializingDependencyNodeVisitor(writer, SerializingDependencyNodeVisitor.STANDARD_TOKENS));
                }
            }
            if (StringUtils.isNotBlank(analysisUrl)) {
                log.info(">>>> remote analysisUrl:" + analysisUrl);
                RemoteInfoWriter remoteInfoWriter = new RemoteInfoWriter(analysisUrl, log);
                node.accept(new RemoteDependencyNodeVisitor(remoteInfoWriter, SerializingDependencyNodeVisitor.STANDARD_TOKENS));
            }
        } catch (Exception e) // Catch all is good enough for IT
        {
            throw new MojoExecutionException("Failed to build dependency graph", e);
        }
    }
}
