package org.poem.maven.plugins.dependency.internal.dto;

import org.apache.maven.artifact.Artifact;

import java.util.List;

/**
 * 依赖结构
 */
public class ArtifactDto {

    /**
     * group id
     */
    private String groupId;

    /**
     * artifact Id
     */
    private String artifactId;
    /**
     * base Version
     */
    private String baseVersion;
    /**
     * Version
     */
    private String version;
    /**
     * scope
     */
    private String scope;
    /**
     * file
     */
    private String file;

    /**
     * 子节点
     */
    private List<ArtifactDto> child;

    public List<ArtifactDto> getChild() {
        return child;
    }

    public void setChild(List<ArtifactDto> child) {
        this.child = child;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getBaseVersion() {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion) {
        this.baseVersion = baseVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    /**
     * 创建vo
     * @param artifact
     * @return
     */
    public static  ArtifactDto build(Artifact artifact){
        ArtifactDto artifactDto = new ArtifactDto();
        artifactDto.setGroupId(artifact.getGroupId());
        artifactDto.setArtifactId(artifact.getArtifactId());
        artifactDto.setBaseVersion(artifact.getBaseVersion());
        artifactDto.setVersion(artifact.getVersion());
        artifactDto.setScope(artifact.getScope());
        artifactDto.setFile(artifact.getArtifactId());
        return artifactDto;

    }

}
