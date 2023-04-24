package org.poem.maven.plugins.spoon.parser;


import org.poem.maven.plugins.spoon.structure.*;

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
    public void create(SpoonParser spoonParser) {
            spoonParser.getClassEntities().forEach(entity -> dao.createClassNode( entity));
            spoonParser.getExecutableEntities().forEach(entity -> {
                if (entity instanceof MethodEntity) {
                    dao.createMethodNode( (MethodEntity) entity);
                } else if (entity instanceof ConstructorEntity) {
                    dao.createConstructorNode( (ConstructorEntity) entity);
                }
            });
            spoonParser.getAbstractInvocationEntities().forEach(entity -> {
                if (entity instanceof InvocationEntity) {
                    dao.createInvocationNode( (InvocationEntity) entity);
                } else if (entity instanceof ConstructorCallEntity) {
                    dao.createConstructorCallNode( (ConstructorCallEntity) entity);
                }
            });
            spoonParser.getTypeParameterEntities().forEach(entity -> dao.createTypeParameterNode( entity));
            spoonParser.getVariableEntities().forEach(entity -> dao.createVariableNode( entity));
            spoonParser.getRelationEntities().forEach(relationEntity -> {
                BaseEntity sourceEntity = relationEntity.getSource();
                BaseEntity targetEntity = relationEntity.getTarget();
                Node sourceNode = dao.getNodeByAstPath( sourceEntity.getEntityType(), sourceEntity.getAstPath());
                Node targetNode = dao.getNodeByAstPath( targetEntity.getEntityType(), targetEntity.getAstPath());
                dao.createRelation(sourceNode, targetNode, relationEntity.getRelation_type());
            });
    }
}
