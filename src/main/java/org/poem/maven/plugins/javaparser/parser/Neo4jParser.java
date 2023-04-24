package org.poem.maven.plugins.javaparser.parser;

import org.poem.maven.plugins.javaparser.dto.EntityRelationDto;
import org.poem.maven.plugins.javaparser.enums.EntityTypeEnum;
import org.poem.maven.plugins.javaparser.enums.RelTypeEnum;
import org.poem.maven.plugins.javaparser.internal.*;
import org.poem.maven.plugins.javaparser.structure.*;
import org.poem.maven.plugins.utils.SnowFlake;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtExecutableReference;

import java.util.stream.Collectors;

/**
 * 解析参数
 */
public class Neo4jParser {


    /**
     * 创建 类节点
     *
     * @param entity
     * @return
     */
    public static ClassNode createClassNode(ClassEntity entity) {
        ClassNode node = new ClassNode();
        node.setLabel(EntityTypeEnum.ClassEntity.toString());
        node.setAstPath(getNotNull(entity.getAstPath()));
        node.setNodeId(SnowFlake.genLongId());
        node.setType(entity.getEntityType());
        node.setName(entity.getName());
        node.setModifiers(getNotNull(entity.getModifiers()));
        node.setInner(entity.isInner());
        node.setShadow(entity.isShadow());
        node.setParameterized(entity.isParameterized());
        node.setSuperClass(entity.getSuperClass() == null ? "" : entity.getSuperClass().getQualifiedName());
        node.setSuperInterfaces(entity
                .getSuperInterfaces()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toSet())
                .toString());
        node.setAllSuperInterfaces(entity
                .getAllSuperClasses()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toSet())
                .toString());
        node.setAllSuperClasses(entity
                .getAllSuperInterfaces()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toSet())
                .toString());
        node.setFields(entity
                .getFields()
                .stream()
                .map(CtVariable::toStringDebug)
                .collect(Collectors.toList())
                .toString());
        node.setMethods(entity
                .getMethods()
                .stream()
                .map(CtExecutable::getSignature)
                .collect(Collectors.toSet())
                .toString());
        node.setConstructors(entity
                .getConstructors()
                .stream()
                .map(CtExecutable::getSignature)
                .collect(Collectors.toSet())
                .toString());
        node.setTypeParameters(entity
                .getTypeParameters()
                .stream()
                .map(CtTypeParameter::toStringDebug)
                .collect(Collectors.toList())
                .toString());
        return node;
    }


    /**
     * 变量实体（对象实体）
     *
     * @param entity
     * @return
     */
    public static VariableNode createVariableNode(VariableEntity entity) {
        VariableNode variableNode = new VariableNode();
        variableNode.setType(entity.getEntityType());
        variableNode.setLabel(EntityTypeEnum.VariableEntity.toString());
        variableNode.setNodeId(SnowFlake.genLongId());
        variableNode.setAstPath(getNotNull(entity.getAstPath()));
        variableNode.setName(getNotNull(entity.getName()));
        variableNode.setArray(entity.isArray());
        variableNode.setOrigin(entity.getOrigin() == null ? "" : entity.getOrigin().getQualifiedName());
        variableNode.setModifiers(getNotNull(entity.getModifiers()));
        variableNode.setAssignment(getNotNull(entity.getAssignment()));
        variableNode.setContainerItems(entity
                .getContainerItems()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toList()));
        return variableNode;
    }


    /**
     * 类型变量实体
     *
     * @param entity
     * @return
     */
    public static TypeParameterNode createTypeParameterNode(TypeParameterEntity entity) {
        TypeParameterNode typeParameterNode = new TypeParameterNode();
        typeParameterNode.setType(entity.getEntityType());
        typeParameterNode.setNodeId(SnowFlake.genLongId());
        typeParameterNode.setLabel(EntityTypeEnum.TypeParameterEntity.toString());
        typeParameterNode.setAstPath(getNotNull(entity.getAstPath()));
        typeParameterNode.setName(getNotNull(entity.getName()));
        {
            CtElement parent = entity.getDeclaration_in();
            if (parent instanceof CtMethod) {
                CtMethod<?> parent_method = (CtMethod<?>) parent;
                typeParameterNode.setDeclarationIn(parent_method.getDeclaringType().getQualifiedName() + "." + parent_method.getSignature());
            } else if (parent instanceof CtTypeInformation) {
                typeParameterNode.setDeclarationIn(((CtTypeInformation) parent).getQualifiedName());
            }
        }
        typeParameterNode.setSuperClass(entity.getSuper_class() == null ? "" : entity.getSuper_class().getQualifiedName());
        typeParameterNode.setSuperInterfaces(entity
                .getSuper_interfaces()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toSet())
                .toString());
        return typeParameterNode;
    }


    /**
     * 方法实体
     *
     * @param entity
     * @return
     */
    public static MethodNode createMethodNode(MethodEntity entity) {
        MethodNode methodNode = new MethodNode();
        methodNode.setType(entity.getEntityType());
        methodNode.setLabel(EntityTypeEnum.MethodEntity.toString());
        methodNode.setNodeId(SnowFlake.genLongId());
        methodNode.setAstPath(getNotNull(entity.getAstPath()));
        methodNode.setFullSignature(getNotNull(entity.getFullSignature()));
        methodNode.setModifiers(getNotNull(entity.getModifiers()));
        methodNode.setDeclarationClass(entity.getDeclarationClass() == null ? "" : entity.getDeclarationClass().getQualifiedName());
        methodNode.setParameters(entity
                .getParameters()
                .stream()
                .map(CtVariable::toStringDebug)
                .collect(Collectors.toList()));
        if (entity.getLocalVariables() != null) {
            methodNode.setLocalVariables(entity
                    .getLocalVariables()
                    .stream()
                    .map(CtVariable::toStringDebug)
                    .collect(Collectors.toList()));
        }
        if (entity.getInvocations() != null) {
            methodNode.setInvocations(entity
                    .getInvocations()
                    .stream()
                    .map(CtAbstractInvocation::toStringDebug)
                    .collect(Collectors.toList()));
        }
        // 以上为通用属性
        methodNode.setName(getNotNull(entity.getName()));
        methodNode.setSignature(getNotNull(entity.getSignature()));
        methodNode.setReturnType(entity.getReturn_type() == null ? "" : entity.getReturn_type().getQualifiedName());
        methodNode.setActualReturnType(entity.getActual_return_type() == null ? "" : entity.getActual_return_type().getQualifiedName());
        methodNode.setTypeParameters(entity
                .getType_parameters()
                .stream()
                .map(CtTypeParameter::toStringDebug)
                .collect(Collectors.toList()));
        methodNode.setGetter(entity.isGetter());
        methodNode.setSetter(entity.isSetter());
        methodNode.setOverrideClasses(entity
                .getOverride_classes()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toSet()));
        return methodNode;
    }


    /**
     * 构造器实体
     *
     * @param entity
     * @return
     */
    public static ConstructorNode createConstructorNode(ConstructorEntity entity) {
        ConstructorNode constructorNode = new ConstructorNode();
        constructorNode.setType(entity.getEntityType());
        constructorNode.setLabel(EntityTypeEnum.ConstructorEntity.toString());
        constructorNode.setNodeId(SnowFlake.genLongId());
        constructorNode.setAstPath(getNotNull(entity.getAstPath()));
        constructorNode.setFullSignature(getNotNull(entity.getFullSignature()));
        constructorNode.setModifiers(getNotNull(entity.getModifiers()));
        constructorNode.setDeclarationClass(entity.getDeclarationClass() == null ? "" : entity.getDeclarationClass().getQualifiedName());
        constructorNode.setParameters(entity
                .getParameters()
                .stream()
                .map(CtVariable::toStringDebug)
                .collect(Collectors.toList()));
        constructorNode.setLocalVariables(entity
                .getLocalVariables()
                .stream()
                .map(CtVariable::toStringDebug)
                .collect(Collectors.toList()));
        constructorNode.setInvocations(entity
                .getInvocations()
                .stream()
                .map(CtAbstractInvocation::toStringDebug)
                .collect(Collectors.toList()));
        return constructorNode;
    }


    /**
     * 方法调用点实体
     *
     * @param entity
     * @return
     */
    public static InvocationNode createInvocationNode(InvocationEntity entity) {
        InvocationNode invocationNode = new InvocationNode();
        invocationNode.setType(entity.getEntityType());
        invocationNode.setNodeId(SnowFlake.genLongId());
        invocationNode.setLabel(EntityTypeEnum.InvocationEntity.toString());
        invocationNode.setAstPath(getNotNull(entity.getAstPath()));
        invocationNode.setCode(getNotNull(entity.getCode()));
        {
            CtElement parent = entity.getDeclarationIn();
            if (parent instanceof CtMethod) {
                CtMethod<?> parentMethod = (CtMethod<?>) parent;
                invocationNode.setDeclaration_in(parentMethod.getDeclaringType().getQualifiedName() + "." + parentMethod.getSignature());
            } else if (parent instanceof CtConstructor) {
                invocationNode.setDeclaration_in(((CtConstructor<?>) parent).getSignature());
            } else if (parent instanceof CtVariable) {
                invocationNode.setDeclaration_in(parent.toStringDebug());
            }
        }
        {
            CtExecutableReference<?> called_executable = entity.getCalledExecutable();
            if (called_executable != null && called_executable.getDeclaringType() != null) {
                String called_class_name = called_executable.getDeclaringType().getQualifiedName();
                invocationNode.setCalled_class(called_class_name);
                invocationNode.setCalled_executable(called_class_name + "." + called_executable.getSignature());
            }
        }
        invocationNode.setArguments(entity
                .getArgumentTypes()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toList()));
        invocationNode.setNext(entity.getNext() == null ? "" : entity.getNext().toStringDebug());
        // 以上为通用属性
        invocationNode.setCallerType(entity.getCallerType().toString());
        invocationNode.setCaller_obj(entity.getCallerObj() == null ? "" : entity.getCallerObj().toStringDebug());
        return invocationNode;
    }


    /**
     * 构造器调用点实体
     *
     * @param entity
     * @return
     */
    public static ConstructorCallNode createConstructorCallNode(ConstructorCallEntity entity) {

        ConstructorCallNode constructorCallNode = new ConstructorCallNode();
        constructorCallNode.setType(entity.getEntityType());
        constructorCallNode.setLabel(EntityTypeEnum.ConstructorCallEntity.toString());
        constructorCallNode.setNodeId(SnowFlake.genLongId());
        constructorCallNode.setAstPath(getNotNull(entity.getAstPath()));
        constructorCallNode.setCode(getNotNull(entity.getCode()));
        {
            CtElement parent = entity.getDeclarationIn();
            if (parent instanceof CtMethod) {
                CtMethod<?> parentMethod = (CtMethod<?>) parent;
                constructorCallNode.setDeclarationIn(parentMethod.getDeclaringType().getQualifiedName() + "." + parentMethod.getSignature());
            } else if (parent instanceof CtConstructor) {
                constructorCallNode.setDeclarationIn(((CtConstructor<?>) parent).getSignature());
            } else if (parent instanceof CtVariable) {
                constructorCallNode.setDeclarationIn(parent.toStringDebug());
            }
        }
        {
            CtExecutableReference<?> calledExecutable = entity.getCalledExecutable();
            if (calledExecutable != null && calledExecutable.getDeclaringType() != null) {
                String calledClassName = calledExecutable.getDeclaringType().getQualifiedName();
                constructorCallNode.setCalledClass(calledClassName);
                constructorCallNode.setCalledExecutable(calledClassName + "." + calledExecutable.getSignature());
            }
        }
        constructorCallNode.setArguments(entity
                .getArgumentTypes()
                .stream()
                .map(CtTypeInformation::getQualifiedName)
                .collect(Collectors.toList()));
        constructorCallNode.setNext(entity.getNext() == null ? "" : entity.getNext().toStringDebug());
        return constructorCallNode;
    }

    private static String getNotNull(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    /**
     * 创建 节点之间的关系
     *
     * @param sourceNode   源节点
     * @param targetNode   目标节点
     * @param relationType 节点关系
     * @return
     */
    public static EntityRelationDto createRelation(Node sourceNode, Node targetNode, RelTypeEnum relationType) {
        EntityRelationDto entityRelationDto = new EntityRelationDto();
        entityRelationDto.setTargetNodeId(sourceNode.getNodeId());
        entityRelationDto.setSourceNodeId(targetNode.getNodeId());
        entityRelationDto.setRelation(relationType.toString());

        return entityRelationDto;
    }
}
