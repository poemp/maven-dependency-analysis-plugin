package org.poem.maven.plugins.javaparser.structure;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;

import java.util.Set;

/**
 * 类型变量实体
 *
 * @author poem
 */
public class TypeParameterEntity extends BaseEntity {
    private String name;
    /**
     * 声明所在的类/方法
     */
    private CtElement declaration_in;
    private CtTypeReference<?> super_class;
    private Set<CtTypeReference<?>> super_interfaces;

    public TypeParameterEntity(String ast_path) {
        super(ast_path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CtElement getDeclaration_in() {
        return declaration_in;
    }

    public void setDeclaration_in(CtElement declaration_in) {
        this.declaration_in = declaration_in;
    }

    public CtTypeReference<?> getSuper_class() {
        return super_class;
    }

    public void setSuper_class(CtTypeReference<?> super_class) {
        this.super_class = super_class;
    }

    public Set<CtTypeReference<?>> getSuper_interfaces() {
        return super_interfaces;
    }

    public void setSuper_interfaces(Set<CtTypeReference<?>> super_interfaces) {
        this.super_interfaces = super_interfaces;
    }

    @Override
    public String toString() {
        return "TypeParameterEntity{\n\t" + super.toString() +
                "\n\t\"name\":\"" +
                name + '\"' +
                ",\n\t\"declaration_in\":" +
                declaration_in +
                ",\n\t\"super_class\":" +
                super_class +
                ",\n\t\"super_interfaces\":" +
                super_interfaces +
                "\n}";
    }
}
