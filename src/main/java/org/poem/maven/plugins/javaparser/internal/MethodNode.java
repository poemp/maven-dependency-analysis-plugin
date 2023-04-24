package org.poem.maven.plugins.javaparser.internal;

import java.util.List;
import java.util.Set;

/**
 * 方法实体
 */
public class MethodNode extends Node{

    private String name;
    private String signature;
    private String returnType;
    /**
     * 可能返回子类
     */
    private String actualReturnType;
    private List<String> typeParameters;
    private boolean getter;
    private boolean setter;
    private String fullSignature;
    private String modifiers;
    private String declarationClass;
    /**
     * 参数列表
     */
    private List<String> parameters;
    /**
     * 局部变量
     */
    private List<String> localVariables;
    private List<String> invocations;

    /**
     * 重写的方法所在类，为空则为自定义的方法
     */
    private Set<String> overrideClasses;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getActualReturnType() {
        return actualReturnType;
    }

    public void setActualReturnType(String actualReturnType) {
        this.actualReturnType = actualReturnType;
    }

    public List<String> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<String> typeParameters) {
        this.typeParameters = typeParameters;
    }

    public boolean isGetter() {
        return getter;
    }

    public void setGetter(boolean getter) {
        this.getter = getter;
    }

    public boolean isSetter() {
        return setter;
    }

    public void setSetter(boolean setter) {
        this.setter = setter;
    }

    public Set<String> getOverrideClasses() {
        return overrideClasses;
    }

    public void setOverrideClasses(Set<String> overrideClasses) {
        this.overrideClasses = overrideClasses;
    }

    public String getFullSignature() {
        return fullSignature;
    }

    public void setFullSignature(String fullSignature) {
        this.fullSignature = fullSignature;
    }

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    public String getDeclarationClass() {
        return declarationClass;
    }

    public void setDeclarationClass(String declarationClass) {
        this.declarationClass = declarationClass;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public List<String> getLocalVariables() {
        return localVariables;
    }

    public void setLocalVariables(List<String> localVariables) {
        this.localVariables = localVariables;
    }

    public List<String> getInvocations() {
        return invocations;
    }

    public void setInvocations(List<String> invocations) {
        this.invocations = invocations;
    }
}
