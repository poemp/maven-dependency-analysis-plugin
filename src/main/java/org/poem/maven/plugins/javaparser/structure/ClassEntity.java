package org.poem.maven.plugins.javaparser.structure;

import org.poem.maven.plugins.javaparser.enums.ClassTypeEnum;
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
    private ClassTypeEnum classType;
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
    private CtTypeReference<?> superClass;
    private Set<CtTypeReference<?>> allSuperClasses;
    private Set<CtTypeReference<?>> superInterfaces;
    private Set<CtTypeReference<?>> allSuperInterfaces;
    private Set<? extends CtConstructor<?>> constructors;
    private List<CtField<?>> fields;
    private Set<CtMethod<?>> methods;
    private List<CtTypeParameter> typeParameters;

    public ClassEntity(String astPath) {
        super(astPath);
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

    public ClassTypeEnum getClassType() {
        return classType;
    }

    public void setClassType(ClassTypeEnum classType) {
        this.classType = classType;
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

    public CtTypeReference<?> getSuperClass() {
        return superClass;
    }

    public void setSuperClass(CtTypeReference<?> superClass) {
        this.superClass = superClass;
    }

    public Set<CtTypeReference<?>> getAllSuperClasses() {
        return allSuperClasses;
    }

    public void setAllSuperClasses(Set<CtTypeReference<?>> allSuperClasses) {
        this.allSuperClasses = allSuperClasses;
    }

    public Set<CtTypeReference<?>> getSuperInterfaces() {
        return superInterfaces;
    }

    public void setSuperInterfaces(Set<CtTypeReference<?>> superInterfaces) {
        this.superInterfaces = superInterfaces;
    }

    public Set<CtTypeReference<?>> getAllSuperInterfaces() {
        return allSuperInterfaces;
    }

    public void setAllSuperInterfaces(Set<CtTypeReference<?>> allSuperInterfaces) {
        this.allSuperInterfaces = allSuperInterfaces;
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

    public List<CtTypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<CtTypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }

    @Override
    public String toString() {
        return "ClassEntity{\n\t" + super.toString() +
                "\n\t\"name\":\"" +
                name + '\"' +
                ",\n\t\"class_type\":" +
                classType +
                ",\n\t\"modifiers\":\"" +
                modifiers + '\"' +
                ",\n\t\"isInner\":" +
                isInner +
                ",\n\t\"isParameterized\":" +
                isParameterized +
                ",\n\t\"isShadow\":" +
                isShadow +
                ",\n\t\"super_class\":" +
                superClass +
                ",\n\t\"all_super_classes\":" +
                allSuperClasses +
                ",\n\t\"super_interfaces\":" +
                superInterfaces +
                ",\n\t\"all_super_interfaces\":" +
                allSuperInterfaces +
                ",\n\t\"constructors\":" +
                constructors +
                ",\n\t\"fields\":" +
                fields +
                ",\n\t\"methods\":" +
                methods +
                ",\n\t\"type_parameters\":" +
                typeParameters +
                "\n}";
    }
}
