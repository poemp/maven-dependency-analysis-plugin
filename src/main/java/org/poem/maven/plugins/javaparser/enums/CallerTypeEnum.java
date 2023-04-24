package org.poem.maven.plugins.javaparser.enums;

/**
 * 方法调用类型枚举：通过某对象调用（调用者为CtVariable）、通过匿名对象调用（调用者为CtAbstractInvocation）、调用静态类的方法（无调用者）
 *
 * @author poem
 */
public enum CallerTypeEnum {
    OBJECT, ANONYMOUS, STATIC, THIS, SUPER, ARRAY,LITERAL
}
