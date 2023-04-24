package org.poem.maven.plugins.spoon.enums;

/**
 * 实体类型枚举：
 * 1. 类实体（类、接口、枚举、注解）
 * 2. 类型参数实体
 * 3. 方法实体
 * 4. 构造器实体
 * 5. 变量实体（成员变量，方法/构造器参数，方法返回值，方法/构造器局部变量）
 * 6. 方法调用点实体
 * 7. 构造器调用点实体
 *
 * @author poem
 */
public enum EntityTypeEnum {
    ClassEntity, TypeParameterEntity, MethodEntity, ConstructorEntity, VariableEntity, InvocationEntity, ConstructorCallEntity
}
