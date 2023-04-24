package org.poem.maven.plugins.javaparser.enums;

/**
 * Neo4j 关系类型枚举
 *
 * @author poem
 */
public enum RelTypeEnum  {
    /**
     * 目标为父类（类-类）
     */
    EXTENDS,
    /**
     * 目标为接口 （类-类）
     */
    IMPLEMENTS,
    /**
     * 字段关联（变量-类）
     */
    ASSOCIATION_TO,
    /**
     * 类的方法（方法-类）
     */
    HAS_METHOD,
    /**
     * 类的成员变量（变量-类）
     */
    HAS_FIELD,
    /**
     * 类的构造器（构造器-类）
     */
    HAS_CONSTRUCTOR,
    /**
     * 类or方法的泛型（类型变量-类、类型变量-方法）
     */
    HAS_GENERIC,
    /**
     * 方法or构造器本地变量（变量-方法、变量-构造器）
     */
    HAS_LOCAL_VARIABLE,
    /**
     * 方法or构造器参数（变量-方法、变量-构造器）
     */
    HAS_PARAMETER,
    /**
     * 方法返回值（变量-方法）
     */
    METHOD_RETURN,
    /**
     * 方法返回值（变量-方法）
     */
    METHOD_ACTUAL_RETURN,
    /**
     * 对抽象方法的实现（方法-方法）
     */
    METHOD_REALIZATION,
    /**
     * 对具体方法的重写（方法-方法）
     */
    METHOD_OVERRIDING,
    /**
     * 发起调用（方法-调用点）
     */
    HAS_INVOKE,
    /**
     * 调用方对象（变量-调用点）
     */
    OBJ,
    /**
     * 被调用方法（方法-调用点）
     */
    CALLED,
    /**
     * （微结构-类/方法/变量/构造器）
     */
    HAS_ROLE,
    NEXT_IN_CHAIN,
    HAS_EXCEPTION,
}
