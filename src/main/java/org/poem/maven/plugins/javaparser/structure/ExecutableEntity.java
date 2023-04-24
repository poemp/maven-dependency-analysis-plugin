package org.poem.maven.plugins.javaparser.structure;

import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;

import java.util.ArrayList;
import java.util.List;

/**
 * 可执行实体，包括方法实体和构造器实体
 *
 * @author poem
 */
public abstract class ExecutableEntity extends BaseEntity {
    private String fullSignature;
    private String modifiers;
    private CtType<?> declarationClass;
    /**
     * 参数列表
     */
    private List<CtParameter<?>> parameters;
    /**
     * 局部变量
     */
    private List<CtLocalVariable<?>> localVariables;
    private List<CtAbstractInvocation<?>> invocations;

    public ExecutableEntity(String ast_path) {
        super(ast_path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutableEntity that = (ExecutableEntity) o;
        return fullSignature.equals(that.fullSignature) || getAstPath().equals(that.getAstPath());
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

    public List<CtParameter<?>> getParameters() {
        return parameters;
    }

    public void setParameters(List<CtParameter<?>> parameters) {
        this.parameters = parameters;
    }

    public List<CtLocalVariable<?>> getLocalVariables() {
        return localVariables == null ? new ArrayList<>() : localVariables;
    }

    public void setLocalVariables(List<CtLocalVariable<?>> localVariables) {
        this.localVariables = localVariables;
    }

    public List<CtAbstractInvocation<?>> getInvocations() {
        return invocations == null ? new ArrayList<>() : invocations;
    }

    public void setInvocations(List<CtAbstractInvocation<?>> invocations) {
        this.invocations = invocations;
    }

    public CtType<?> getDeclarationClass() {
        return declarationClass;
    }

    public void setDeclarationClass(CtType<?> declarationClass) {
        this.declarationClass = declarationClass;
    }

    @Override
    public String toString() {
        return "ExecutableEntity{\n\t" + super.toString() +
                "\n\t\"full_signature\":\"" +
                fullSignature + '\"' +
                ",\n\t\"modifiers\":\"" +
                modifiers + '\"' +
                ",\n\t\"declaration_class\":" +
                declarationClass +
                ",\n\t\"parameters\":" +
                parameters +
                ",\n\t\"local_variables\":" +
                localVariables +
                ",\n\t\"invocations\":" +
                invocations +
                "\n}";
    }
}
