package org.poem.maven.plugins.javaparser.internal;

public class Node {

    /**
     * 节点的唯一id
     */
    private Long nodeId;
    /**
     * 数据节点的 Label
     */
    private String label;


    /**
     * 节点类型
     */
    private String type;

    private String astPath;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAstPath() {
        return astPath;
    }

    public void setAstPath(String astPath) {
        this.astPath = astPath;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
