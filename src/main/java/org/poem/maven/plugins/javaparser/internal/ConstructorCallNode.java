package org.poem.maven.plugins.javaparser.internal;

import java.util.List;

/**
 * 构造器调用点实体
 */
public class ConstructorCallNode extends Node{


    private String code;
    /**
     * 可能在方法、构造器、变量内
     */
    private String declarationIn;
    /**
     * 目标方法/构造器
     */
    private String calledExecutable;
    /**
     * 目标方法/构造器所在的类/接口
     */
    private String calledClass;

    /**
     * 参数类型
     */
    private List<String> argumentTypes;
    /**
     * 链式调用的下一个调用点，自身是调用点且 RoleInParent 是 target，则 getParent 可获得下一个调用点
     */
    private String next;


    private List<String> arguments;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeclarationIn() {
        return declarationIn;
    }

    public void setDeclarationIn(String declarationIn) {
        this.declarationIn = declarationIn;
    }

    public String getCalledExecutable() {
        return calledExecutable;
    }

    public void setCalledExecutable(String calledExecutable) {
        this.calledExecutable = calledExecutable;
    }

    public String getCalledClass() {
        return calledClass;
    }

    public void setCalledClass(String calledClass) {
        this.calledClass = calledClass;
    }

    public List<String> getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(List<String> argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }
}
