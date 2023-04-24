package org.poem.maven.plugins.spoon.structure;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;

/**
 * 抽象调用点实体，包括方法调用点实体、构造器调用点实体
 *
 * @author poem
 */
public abstract class AbstractInvocationEntity extends BaseEntity {
    private String code;
    /**
     * 可能在方法、构造器、变量内
     */
    private CtElement declaration_in;
    /**
     * 目标方法/构造器
     */
    private CtExecutableReference<?> called_executable;
    /**
     * 目标方法/构造器所在的类/接口
     */
    private CtTypeReference<?> called_class;
    private List<CtTypeReference<?>> argument_types;
    /**
     * 链式调用的下一个调用点，自身是调用点且 RoleInParent 是 target，则 getParent 可获得下一个调用点
     */
    private CtInvocation<?> next;

    public AbstractInvocationEntity(String ast_path) {
        super(ast_path);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CtElement getDeclaration_in() {
        return declaration_in;
    }

    public void setDeclaration_in(CtElement declaration_in) {
        this.declaration_in = declaration_in;
    }

    public CtExecutableReference<?> getCalled_executable() {
        return called_executable;
    }

    public void setCalled_executable(CtExecutableReference<?> called_executable) {
        this.called_executable = called_executable;
    }

    public CtTypeReference<?> getCalled_class() {
        return called_class;
    }

    public void setCalled_class(CtTypeReference<?> called_class) {
        this.called_class = called_class;
    }

    public List<CtTypeReference<?>> getArgument_types() {
        return argument_types;
    }

    public void setArgument_types(List<CtTypeReference<?>> argument_types) {
        this.argument_types = argument_types;
    }

    public CtInvocation<?> getNext() {
        return next;
    }

    public void setNext(CtInvocation<?> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "AbstractInvocationEntity{\n\t" + super.toString() +
                "\n\t\"code\":\"" +
                code + '\"' +
                ",\n\t\"declaration_in\":" +
                declaration_in +
                ",\n\t\"called_executable\":" +
                called_executable +
                ",\n\t\"called_class\":" +
                called_class +
                ",\n\t\"argument_types\":" +
                argument_types +
                ",\n\t\"next\":" +
                next +
                "\n}";
    }
}
