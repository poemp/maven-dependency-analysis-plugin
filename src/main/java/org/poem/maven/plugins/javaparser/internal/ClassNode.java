package org.poem.maven.plugins.javaparser.internal;

/**
 * 类实体
 */
public class ClassNode extends Node{

    private String astPath;

    private String name;

    /**
     * 修饰符
     */
    private String modifiers;

    /**
     * 是内部类
     */
    private  boolean inner;

    /**
     * 属于引用其他模块的类？（包括JDK）
     */
    private boolean shadow;

    /**
     * 可泛化，如java.util.Map<K,V>，设置该字段以便于分析容器
     */
    private boolean parameterized;

    private String superClass;
    private String allSuperClasses;
    private String superInterfaces;
    private String allSuperInterfaces;
    private String constructors;
    private String fields;
    private String methods;
    private String typeParameters;

    public String getAstPath() {
        return astPath;
    }

    public void setAstPath(String astPath) {
        this.astPath = astPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    public boolean isInner() {
        return inner;
    }

    public void setInner(boolean inner) {
        this.inner = inner;
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public boolean isParameterized() {
        return parameterized;
    }

    public void setParameterized(boolean parameterized) {
        this.parameterized = parameterized;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public String getAllSuperClasses() {
        return allSuperClasses;
    }

    public void setAllSuperClasses(String allSuperClasses) {
        this.allSuperClasses = allSuperClasses;
    }

    public String getSuperInterfaces() {
        return superInterfaces;
    }

    public void setSuperInterfaces(String superInterfaces) {
        this.superInterfaces = superInterfaces;
    }

    public String getAllSuperInterfaces() {
        return allSuperInterfaces;
    }

    public void setAllSuperInterfaces(String allSuperInterfaces) {
        this.allSuperInterfaces = allSuperInterfaces;
    }

    public String getConstructors() {
        return constructors;
    }

    public void setConstructors(String constructors) {
        this.constructors = constructors;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(String typeParameters) {
        this.typeParameters = typeParameters;
    }
}
