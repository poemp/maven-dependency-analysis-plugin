package org.poem.maven.plugins.javaparser.structure;

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
    private CtElement declarationIn;
    /**
     * 目标方法/构造器
     */
    private CtExecutableReference<?> calledExecutable;
    /**
     * 目标方法/构造器所在的类/接口
     */
    private CtTypeReference<?> calledClass;
    private List<CtTypeReference<?>> argumentTypes;
    /**
     * 链式调用的下一个调用点，自身是调用点且 RoleInParent 是 target，则 getParent 可获得下一个调用点
     */
    private CtInvocation<?> next;

    public AbstractInvocationEntity(String astPath) {
        super(astPath);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CtElement getDeclarationIn() {
        return declarationIn;
    }

    public void setDeclarationIn(CtElement declarationIn) {
        this.declarationIn = declarationIn;
    }

    public CtExecutableReference<?> getCalledExecutable() {
        return calledExecutable;
    }

    public void setCalledExecutable(CtExecutableReference<?> calledExecutable) {
        this.calledExecutable = calledExecutable;
    }

    public CtTypeReference<?> getCalledClass() {
        return calledClass;
    }

    public void setCalledClass(CtTypeReference<?> calledClass) {
        this.calledClass = calledClass;
    }

    public List<CtTypeReference<?>> getArgumentTypes() {
        return argumentTypes;
    }

    public void setArgumentTypes(List<CtTypeReference<?>> argumentTypes) {
        this.argumentTypes = argumentTypes;
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
                declarationIn +
                ",\n\t\"called_executable\":" +
                calledExecutable +
                ",\n\t\"called_class\":" +
                calledClass +
                ",\n\t\"argument_types\":" +
                argumentTypes +
                ",\n\t\"next\":" +
                next +
                "\n}";
    }
}
