package org.poem.maven.plugins.javaparser.internal;

import java.util.List;

/**
 * 构造器实体
 */
public class ConstructorNode extends Node{


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
