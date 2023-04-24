package org.poem.maven.plugins.spoon.structure;

import org.poem.maven.plugins.spoon.enums.CallerTypeEnum;
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
    private CtElement caller_obj;

    public InvocationEntity(String ast_path) {
        super(ast_path);
    }

    public CallerTypeEnum getCallerType() {
        return callerType;
    }

    public void setCallerType(CallerTypeEnum callerType) {
        this.callerType = callerType;
    }

    public CtElement getCaller_obj() {
        return caller_obj;
    }

    public void setCaller_obj(CtElement caller_obj) {
        this.caller_obj = caller_obj;
    }

    @Override
    public String toString() {
        return "InvocationEntity{\n\t" + super.toString() +
                "\n\t\"callerType\":" +
                callerType +
                ",\n\t\"caller_obj\":" +
                caller_obj +
                "\n}";
    }
}
