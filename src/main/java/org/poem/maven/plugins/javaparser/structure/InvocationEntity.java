package org.poem.maven.plugins.javaparser.structure;

import org.poem.maven.plugins.javaparser.enums.CallerTypeEnum;
import spoon.reflect.declaration.CtElement;

/**
 * 方法调用点实体
 *
 * @author poem
 */
public class InvocationEntity extends AbstractInvocationEntity {
    private CallerTypeEnum callerType;
    /**
     *  1.静态方法调用 调用者为null 2.对象调用，调用者为CtVariable 3.匿名对象调用，调用者为CtAbstractInvocation
     */
    private CtElement callerObj;

    public InvocationEntity(String astPath) {
        super(astPath);
    }

    public CallerTypeEnum getCallerType() {
        return callerType;
    }

    public void setCallerType(CallerTypeEnum callerType) {
        this.callerType = callerType;
    }

    public CtElement getCallerObj() {
        return callerObj;
    }

    public void setCallerObj(CtElement callerObj) {
        this.callerObj = callerObj;
    }

    @Override
    public String toString() {
        return "InvocationEntity{\n\t" + super.toString() +
                "\n\t\"callerType\":" +
                callerType +
                ",\n\t\"caller_obj\":" +
                callerObj +
                "\n}";
    }
}
