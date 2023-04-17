package org.poem.maven.plugins.dependency.traversal;

import com.alibaba.fastjson2.JSONObject;
import org.apache.maven.artifact.Artifact;
import org.poem.maven.plugins.dependency.DependencyNode;
import org.poem.maven.plugins.dependency.internal.dto.ArtifactDto;
import org.poem.maven.plugins.dependency.remote.RemoteInfoWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 远程信息
 * remote analysis plugin
 *
 * @author poem
 */
public class RemoteDependencyNodeVisitor implements DependencyNodeVisitor {

    /**
     * The writer to serialize to.
     */
    private final RemoteInfoWriter writer;


    public RemoteDependencyNodeVisitor(RemoteInfoWriter writer, SerializingDependencyNodeVisitor.GraphTokens tokens) {
        this.writer = writer;
    }

    /**
     * 解析
     * @param dependencyNodes
     * @return
     */
    private List<ArtifactDto> visit(List<DependencyNode> dependencyNodes) throws IOException {
        List<ArtifactDto> artifactDtos = new ArrayList<>();
        for (DependencyNode dependencyNode : dependencyNodes) {
            ArtifactDto artifactDto = ArtifactDto.build(dependencyNode.getArtifact());
            if (!dependencyNode.accept(this)) {
                artifactDtos.add(artifactDto);
                break;
            }
            List<DependencyNode> children = dependencyNode.getChildren();
            if (children != null && children.size() > 0){
                List<ArtifactDto> childrenArtifacts = visit(children);
                artifactDto.setChild(childrenArtifacts);
                artifactDtos.add(artifactDto);
            }
        }
        return artifactDtos;
    }

    /**
     * 解析数据
     *
     * @param node the dependency node to visit
     * @return
     * @throws IOException
     */
    @Override
    public boolean visit(DependencyNode node) throws IOException {
        List<ArtifactDto> artifactDtos = new ArrayList<>();
        Artifact artifact = node.getArtifact();
        ArtifactDto artifactDto = ArtifactDto.build(artifact);
        artifactDto.setChild(visit(node.getChildren()));
        artifactDtos.add(artifactDto);
        writer.write(JSONObject.toJSONString(artifactDtos));
        return true;
    }

    @Override
    public boolean endVisit(DependencyNode node) {

        return true;
    }
}
