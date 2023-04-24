package org.poem.maven.plugins.javaparser.internal;

/**
 * 类型变量实体
 */
public class TypeParameterNode extends Node{

    private String astPath;
    private String name;
    /**
     * 声明所在的类/方法
     */
    private String declarationIn;
    private String superClass;
    private String superInterfaces;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAstPath() {
        return astPath;
    }

    public void setAstPath(String astPath) {
        this.astPath = astPath;
    }

    public String getDeclarationIn() {
        return declarationIn;
    }

    public void setDeclarationIn(String declarationIn) {
        this.declarationIn = declarationIn;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public String getSuperInterfaces() {
        return superInterfaces;
    }

    public void setSuperInterfaces(String superInterfaces) {
        this.superInterfaces = superInterfaces;
    }
}
