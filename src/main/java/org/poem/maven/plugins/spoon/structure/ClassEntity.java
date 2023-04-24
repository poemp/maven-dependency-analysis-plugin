package org.poem.maven.plugins.spoon.structure;

import org.poem.maven.plugins.spoon.enums.ClassTypeEnum;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 类实体
 *
 * @author poem
 */
public class ClassEntity extends BaseEntity {

    /**
     * 全限定名
     */
    private String name;
    /**
     * 类的类型：类/接口/枚举/注解
     */
    private ClassTypeEnum class_type;
    /**
     * 修饰符
     */
    private String modifiers;
    /**
     * 是内部类
     */
    private boolean isInner;
    /**
     * 可泛化，如java.util.Map<K,V>，设置该字段以便于分析容器
     */
    private boolean isParameterized;
    /**
     * 属于引用其他模块的类？（包括JDK）
     */
    private boolean isShadow;
    /**
     * 类其他结构
     */
    private CtTypeReference<?> super_class;
    private Set<CtTypeReference<?>> all_super_classes;
    private Set<CtTypeReference<?>> super_interfaces;
    private Set<CtTypeReference<?>> all_super_interfaces;
    private Set<? extends CtConstructor<?>> constructors;
    private List<CtField<?>> fields;
    private Set<CtMethod<?>> methods;
    private List<CtTypeParameter> type_parameters;

    public ClassEntity(String ast_path) {
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
        if (!super.equals(o)) {
            return false;
        }
        ClassEntity that = (ClassEntity) o;
        return Objects.equals(name, that.name) || getAstPath().equals(that.getAstPath());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassTypeEnum getClass_type() {
        return class_type;
    }

    public void setClass_type(ClassTypeEnum class_type) {
        this.class_type = class_type;
    }

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    public boolean isInner() {
        return isInner;
    }

    public void setInner(boolean inner) {
        isInner = inner;
    }

    public boolean isParameterized() {
        return isParameterized;
    }

    public void setParameterized(boolean parameterized) {
        isParameterized = parameterized;
    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setShadow(boolean shadow) {
        isShadow = shadow;
    }

    public CtTypeReference<?> getSuper_class() {
        return super_class;
    }

    public void setSuper_class(CtTypeReference<?> super_class) {
        this.super_class = super_class;
    }

    public Set<CtTypeReference<?>> getAll_super_classes() {
        return all_super_classes;
    }

    public void setAll_super_classes(Set<CtTypeReference<?>> all_super_classes) {
        this.all_super_classes = all_super_classes;
    }

    public Set<CtTypeReference<?>> getSuper_interfaces() {
        return super_interfaces;
    }

    public void setSuper_interfaces(Set<CtTypeReference<?>> super_interfaces) {
        this.super_interfaces = super_interfaces;
    }

    public Set<CtTypeReference<?>> getAll_super_interfaces() {
        return all_super_interfaces;
    }

    public void setAll_super_interfaces(Set<CtTypeReference<?>> all_super_interfaces) {
        this.all_super_interfaces = all_super_interfaces;
    }

    public Set<? extends CtConstructor<?>> getConstructors() {
        return constructors;
    }

    public void setConstructors(Set<? extends CtConstructor<?>> constructors) {
        this.constructors = constructors;
    }

    public List<CtField<?>> getFields() {
        return fields;
    }

    public void setFields(List<CtField<?>> fields) {
        this.fields = fields;
    }

    public Set<CtMethod<?>> getMethods() {
        return methods;
    }

    public void setMethods(Set<CtMethod<?>> methods) {
        this.methods = methods;
    }

    public List<CtTypeParameter> getType_parameters() {
        return type_parameters;
    }

    public void setType_parameters(List<CtTypeParameter> type_parameters) {
        this.type_parameters = type_parameters;
    }

    @Override
    public String toString() {
        return "ClassEntity{\n\t" + super.toString() +
                "\n\t\"name\":\"" +
                name + '\"' +
                ",\n\t\"class_type\":" +
                class_type +
                ",\n\t\"modifiers\":\"" +
                modifiers + '\"' +
                ",\n\t\"isInner\":" +
                isInner +
                ",\n\t\"isParameterized\":" +
                isParameterized +
                ",\n\t\"isShadow\":" +
                isShadow +
                ",\n\t\"super_class\":" +
                super_class +
                ",\n\t\"all_super_classes\":" +
                all_super_classes +
                ",\n\t\"super_interfaces\":" +
                super_interfaces +
                ",\n\t\"all_super_interfaces\":" +
                all_super_interfaces +
                ",\n\t\"constructors\":" +
                constructors +
                ",\n\t\"fields\":" +
                fields +
                ",\n\t\"methods\":" +
                methods +
                ",\n\t\"type_parameters\":" +
                type_parameters +
                "\n}";
    }
}
