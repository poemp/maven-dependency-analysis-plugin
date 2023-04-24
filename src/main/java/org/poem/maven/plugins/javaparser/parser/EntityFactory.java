package org.poem.maven.plugins.javaparser.parser;

import org.poem.maven.plugins.javaparser.enums.EntityTypeEnum;
import org.poem.maven.plugins.javaparser.enums.RelTypeEnum;
import org.poem.maven.plugins.javaparser.enums.VariableScopeEnum;
import org.poem.maven.plugins.javaparser.relation.RelationEntity;
import org.poem.maven.plugins.javaparser.structure.*;

import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 实体创建工厂类
 *
 * @author poem
 */
public interface EntityFactory {

    Set<ClassEntity> CLASS_ENTITY_SET = new HashSet<>();
    Set<ExecutableEntity> EXECUTABLE_ENTITY_SET = new HashSet<>();
    Set<VariableEntity> VARIABLE_ENTITY_SET = new HashSet<>();
    Set<AbstractInvocationEntity> ABSTRACT_INVOCATION_ENTITY_SET = new HashSet<>();
    Set<TypeParameterEntity> TYPE_PARAMETER_ENTITY_SET = new HashSet<>();
    Set<RelationEntity> RELATIONSHIP_SET = new HashSet<>();

    /**
     * 创建类实体
     * 注意调用此方法前先执行 SpoonUtil.isValid() 进行验证
     *
     * @param ctType 类对应的spoon类型
     * @return 类实体
     */
    ClassEntity createClassEntity(CtType<?> ctType);

    /**
     * 创建构造器实体
     *
     * @param ctConstructor 构造器对应的spoon类型
     * @return 构造器实体
     */
    ConstructorEntity createConstructorEntity(CtConstructor<?> ctConstructor);

    /**
     * 创建方法实体
     *
     * @param ctMethod 方法对应的spoon类型
     * @return 方法实体
     */
    MethodEntity createMethodEntity(CtMethod<?> ctMethod);

    /**
     * 创建类型变量实体
     *
     * @return 类实体
     */
    TypeParameterEntity createTypeParameterEntity(CtTypeParameter ctTypeParameter);

    /**
     * 创建变量实体
     *
     * @param ctVariable 变量对应的spoon类型
     * @param scope      变量作用域：成员变量/参数列表/局部变量/方法返回值
     * @return 变量实体
     */
    VariableEntity createVariableEntity(CtVariable<?> ctVariable, VariableScopeEnum scope);

    /**
     * 创建方法实体
     *
     * @param source  源实体
     * @param target  目标实体
     * @param relType 关系类型
     * @return 创建的方法实体
     */
    RelationEntity createRelationEntity(BaseEntity source, BaseEntity target, RelTypeEnum relType);

    RelationEntity createRelationEntity(BaseEntity source, BaseEntity target, RelTypeEnum relType, Map<String, String> properties);

    RelationEntity addRelationProperty(RelationEntity entity, String key, String val);

    /**
     * 创建方法调用点实体
     *
     * @param ctInvocation 方法调用点
     * @return 创建的实体
     */
    InvocationEntity createInvocationEntity(CtInvocation<?> ctInvocation);

    /**
     * 创建构造器调用点实体
     *
     * @param ctConstructorCall 构造器调用点
     * @return 创建的实体
     */
    ConstructorCallEntity createConstructorCallEntity(CtConstructorCall<?> ctConstructorCall);

    /**
     * 通过全限定名获取类实体
     *
     * @param name 类的全限定名
     * @return 类实体
     */
    ClassEntity getClassEntityByQualifiedName(String name);

    /**
     * 通过签名列表获得方法实体或构造器实体
     *
     * @param full_signature 方法/构造器的完整签名列表（含全限定名，可作为方法的索引）
     * @return 方法实体/构造器实体
     */
    ExecutableEntity getExecutableEntityBySignature(String full_signature);

    /**
     * 通过抽象语法树路径获取实体
     *
     * @param entityType 实体类型
     * @param ast_path   抽象语法树路径
     * @return 获得的实体
     */
    BaseEntity getEntityByAstPath(EntityTypeEnum entityType, String ast_path);
}

