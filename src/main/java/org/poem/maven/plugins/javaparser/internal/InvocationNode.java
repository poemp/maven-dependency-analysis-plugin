package org.poem.maven.plugins.javaparser.internal;

import java.util.List;

/**
 * 方法调用点实体
 */
public class InvocationNode extends Node{

    private String code;
    /**
     * 可能在方法、构造器、变量内
     */
    private String declaration_in;
    /**
     * 目标方法/构造器
     */
    private String called_executable;
    /**
     * 目标方法/构造器所在的类/接口
     */
    private String called_class;

    private List<String> argument_types;
    /**
     * 链式调用的下一个调用点，自身是调用点且 RoleInParent 是 target，则 getParent 可获得下一个调用点
     */
    private String next;

    /**
     * 参数
     */
    private List<String> arguments;


    private String callerType;
    /**
     *  1.静态方法调用 调用者为null 2.对象调用，调用者为CtVariable 3.匿名对象调用，调用者为CtAbstractInvocation
     */
    private String caller_obj;


    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeclaration_in() {
        return declaration_in;
    }

    public void setDeclaration_in(String declaration_in) {
        this.declaration_in = declaration_in;
    }

    public String getCalled_executable() {
        return called_executable;
    }

    public void setCalled_executable(String called_executable) {
        this.called_executable = called_executable;
    }

    public String getCalled_class() {
        return called_class;
    }

    public void setCalled_class(String called_class) {
        this.called_class = called_class;
    }

    public List<String> getArgument_types() {
        return argument_types;
    }

    public void setArgument_types(List<String> argument_types) {
        this.argument_types = argument_types;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getCallerType() {
        return callerType;
    }

    public void setCallerType(String callerType) {
        this.callerType = callerType;
    }

    public String getCaller_obj() {
        return caller_obj;
    }

    public void setCaller_obj(String caller_obj) {
        this.caller_obj = caller_obj;
    }
}
