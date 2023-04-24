package org.poem.maven.plugins.javaparser.dto;

import com.google.common.collect.Maps;
import org.poem.maven.plugins.javaparser.internal.*;

import java.util.List;
import java.util.Map;

/**
 * 返回
 */
public class ClassDto {

    /**
     * 类实体
     */
    private List<ClassNode> classNodes;

    /**
     * 方法实体
     */
    private List<MethodNode> methodNodes;

    /**
     * 构造器实体
     */
    private List<ConstructorNode> constructorNodes;


    /**
     * \
     * 方法调用点实体
     */
    private List<InvocationNode> invocationNodes;

    /**
     * 构造器调用点实体
     */
    private List<ConstructorCallNode> constructorCallNodes;

    /**
     * 类型变量实体
     */
    private List<TypeParameterNode> typeParameterNodes;

    /**
     * 变量实体
     */
    private List<VariableNode> variableNodes;


    /**
     * 节点之间的关系
     */
    private List<EntityRelationDto> relationDtoList;

    public List<ClassNode> getClassNodes() {
        return classNodes;
    }

    public void setClassNodes(List<ClassNode> classNodes) {
        this.classNodes = classNodes;
    }

    public List<MethodNode> getMethodNodes() {
        return methodNodes;
    }

    public void setMethodNodes(List<MethodNode> methodNodes) {
        this.methodNodes = methodNodes;
    }

    public List<ConstructorNode> getConstructorNodes() {
        return constructorNodes;
    }

    public void setConstructorNodes(List<ConstructorNode> constructorNodes) {
        this.constructorNodes = constructorNodes;
    }

    public List<InvocationNode> getInvocationNodes() {
        return invocationNodes;
    }

    public void setInvocationNodes(List<InvocationNode> invocationNodes) {
        this.invocationNodes = invocationNodes;
    }

    public List<ConstructorCallNode> getConstructorCallNodes() {
        return constructorCallNodes;
    }

    public void setConstructorCallNodes(List<ConstructorCallNode> constructorCallNodes) {
        this.constructorCallNodes = constructorCallNodes;
    }

    public List<TypeParameterNode> getTypeParameterNodes() {
        return typeParameterNodes;
    }

    public void setTypeParameterNodes(List<TypeParameterNode> typeParameterNodes) {
        this.typeParameterNodes = typeParameterNodes;
    }

    public List<VariableNode> getVariableNodes() {
        return variableNodes;
    }

    public void setVariableNodes(List<VariableNode> variableNodes) {
        this.variableNodes = variableNodes;
    }

    public List<EntityRelationDto> getRelationDtoList() {
        return relationDtoList;
    }

    public void setRelationDtoList(List<EntityRelationDto> relationDtoList) {
        this.relationDtoList = relationDtoList;
    }

    /**
     * 节点
     *
     * @return
     */
    public Map<String, Node> parserNodeTotypeAndAstPathMap() {
        Map<String, Node> map = Maps.newHashMap();
        classNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        methodNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        constructorNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        invocationNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        constructorCallNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        typeParameterNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        variableNodes.forEach((Node node) -> map.put(node.getType() + "@" + node.getAstPath(), node));
        return map;
    }
}
