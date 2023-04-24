package org.poem.maven.plugins.spoon.structure;

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
    private String full_signature;
    private String modifiers;
    private CtType<?> declaration_class;
    /**
     * 参数列表
     */
    private List<CtParameter<?>> parameters;
    /**
     * 局部变量
     */
    private List<CtLocalVariable<?>> local_variables;
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
        return full_signature.equals(that.full_signature) || getAstPath().equals(that.getAstPath());
    }

    public String getFull_signature() {
        return full_signature;
    }

    public void setFull_signature(String full_signature) {
        this.full_signature = full_signature;
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

    public List<CtLocalVariable<?>> getLocal_variables() {
        return local_variables == null ? new ArrayList<>() : local_variables;
    }

    public void setLocal_variables(List<CtLocalVariable<?>> local_variables) {
        this.local_variables = local_variables;
    }

    public List<CtAbstractInvocation<?>> getInvocations() {
        return invocations == null ? new ArrayList<>() : invocations;
    }

    public void setInvocations(List<CtAbstractInvocation<?>> invocations) {
        this.invocations = invocations;
    }

    public CtType<?> getDeclaration_class() {
        return declaration_class;
    }

    public void setDeclaration_class(CtType<?> declaration_class) {
        this.declaration_class = declaration_class;
    }

    @Override
    public String toString() {
        return "ExecutableEntity{\n\t" + super.toString() +
                "\n\t\"full_signature\":\"" +
                full_signature + '\"' +
                ",\n\t\"modifiers\":\"" +
                modifiers + '\"' +
                ",\n\t\"declaration_class\":" +
                declaration_class +
                ",\n\t\"parameters\":" +
                parameters +
                ",\n\t\"local_variables\":" +
                local_variables +
                ",\n\t\"invocations\":" +
                invocations +
                "\n}";
    }
}
