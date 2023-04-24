package org.poem.maven.plugins.spoon.structure;

import org.poem.maven.plugins.spoon.enums.VariableScopeEnum;
import spoon.reflect.code.CtExpression;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;

/**
 * 变量实体（对象实体）
 *
 * @author poem
 */
public class VariableEntity extends BaseEntity {
    private boolean isArray;
    private VariableScopeEnum scope;
    /**
     * 容器元素类型，为null则不是容器
     */
    private List<CtTypeReference<?>> container_items;
    private String name;
    /**
     * 原始类，若为非基本元素非Object数组：创建关系时直接指向元素类，否则不创建关系
     */
    private CtTypeReference<?> origin;
    private String modifiers;
    private CtExpression<?> assignment;

    public VariableEntity(String ast_path) {
        super(ast_path);
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public VariableScopeEnum getScope() {
        return scope;
    }

    public void setScope(VariableScopeEnum scope) {
        this.scope = scope;
    }

    public List<CtTypeReference<?>> getContainer_items() {
        return container_items;
    }

    public void setContainer_items(List<CtTypeReference<?>> container_items) {
        this.container_items = container_items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CtTypeReference<?> getOrigin() {
        return origin;
    }

    public void setOrigin(CtTypeReference<?> origin) {
        this.origin = origin;
    }

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    public CtExpression<?> getAssignment() {
        return assignment;
    }

    public void setAssignment(CtExpression<?> assignment) {
        this.assignment = assignment;
    }

    @Override
    public String toString() {
        return "VariableEntity{\n\t" + super.toString() +
                "\n\t\"isArray\":" +
                isArray +
                ",\n\t\"scope\":" +
                scope +
                ",\n\t\"container_items\":" +
                container_items +
                ",\n\t\"name\":\"" +
                name + '\"' +
                ",\n\t\"origin\":" +
                origin +
                ",\n\t\"modifiers\":\"" +
                modifiers + '\"' +
                ",\n\t\"assignment\":" +
                assignment +
                "\n}";
    }
}
