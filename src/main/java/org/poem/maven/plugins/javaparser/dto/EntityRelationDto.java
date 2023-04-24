package org.poem.maven.plugins.javaparser.dto;

public class EntityRelationDto {

    /**
     * 源节点id
     */
    private Long targetNodeId;

    /**
     * 目标节点id
     */
    private Long sourceNodeId;

    /**
     * 关系类型
     */
    private String relation;

    public Long getTargetNodeId() {
        return targetNodeId;
    }

    public void setTargetNodeId(Long targetNodeId) {
        this.targetNodeId = targetNodeId;
    }

    public Long getSourceNodeId() {
        return sourceNodeId;
    }

    public void setSourceNodeId(Long sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
