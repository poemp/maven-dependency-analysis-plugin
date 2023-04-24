package org.poem.maven.plugins.javaparser.parser;


import com.google.common.collect.Lists;
import org.poem.maven.plugins.javaparser.dto.ClassDto;
import org.poem.maven.plugins.javaparser.dto.EntityRelationDto;
import org.poem.maven.plugins.javaparser.internal.*;
import org.poem.maven.plugins.javaparser.structure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Neo4j 解析器，将实体映射为节点和关系
 *
 * @author poem
 */
public class NeoParser {

    private static final NeoParser instance = new NeoParser();

    public static NeoParser getInstance() {
        return instance;
    }
    /**
     * 解析后使用，通过 spoonParser 引用的节点实体、方法实体创建图
     *
     * @param spoonParser 解析管理器实例
     */
    public ClassDto create(SpoonParser spoonParser) {
        List<ClassNode> classNodes = spoonParser.getClassEntities().stream().map(Neo4jParser::createClassNode).collect(Collectors.toList());
        List<MethodNode> methodNodes = Lists.newArrayList();
        List<ConstructorNode> constructorNodes = Lists.newArrayList();
        List<InvocationNode> invocationNodes = Lists.newArrayList();
        List<ConstructorCallNode> constructorCallNodes = Lists.newArrayList();

        spoonParser.getExecutableEntities().forEach(entity -> {
            if (entity instanceof MethodEntity) {
                MethodNode methodNode = Neo4jParser.createMethodNode((MethodEntity) entity);
                methodNodes.add(methodNode);
            } else if (entity instanceof ConstructorEntity) {
                ConstructorNode constructorNode = Neo4jParser.createConstructorNode((ConstructorEntity) entity);
                constructorNodes.add(constructorNode);
            }
        });
        spoonParser.getAbstractInvocationEntities().forEach(entity -> {
            if (entity instanceof InvocationEntity) {
                InvocationNode invocationNode = Neo4jParser.createInvocationNode((InvocationEntity) entity);
                invocationNodes.add(invocationNode);
            } else if (entity instanceof ConstructorCallEntity) {
                ConstructorCallNode constructorCallNode = Neo4jParser.createConstructorCallNode((ConstructorCallEntity) entity);
                constructorCallNodes.add(constructorCallNode);
            }
        });
        //类型变量实体
        List<TypeParameterNode> typeParameterNodes = spoonParser.getTypeParameterEntities().stream().map(Neo4jParser::createTypeParameterNode).collect(Collectors.toList());
        //变量实体
        List<VariableNode> variableNodes = spoonParser.getVariableEntities().stream().map(Neo4jParser::createVariableNode).collect(Collectors.toList());

        ClassDto classDto = new ClassDto();
        classDto.setClassNodes(classNodes);
        classDto.setMethodNodes(methodNodes);
        classDto.setConstructorNodes(constructorNodes);
        classDto.setInvocationNodes(invocationNodes);
        classDto.setConstructorCallNodes(constructorCallNodes);
        classDto.setTypeParameterNodes(typeParameterNodes);
        classDto.setVariableNodes(variableNodes);
        Map<String, Node> map = classDto.parserNodeTotypeAndAstPathMap();

        List<EntityRelationDto> entityRelations = new ArrayList<>(spoonParser.getRelationEntities().size());

        spoonParser.getRelationEntities().forEach(relationEntity -> {
            BaseEntity sourceEntity = relationEntity.getSource();
            BaseEntity targetEntity = relationEntity.getTarget();
            Node sourceNode = map.get(sourceEntity.getEntityType()+"@"+sourceEntity.getAstPath());
            Node targetNode = map.get(targetEntity.getEntityType()+"@"+targetEntity.getAstPath());
            EntityRelationDto entityRelationDto = Neo4jParser.createRelation(sourceNode, targetNode, relationEntity.getRelationType());
            entityRelations.add(entityRelationDto);
        });
        classDto.setRelationDtoList(entityRelations);
        return classDto;
    }
}
