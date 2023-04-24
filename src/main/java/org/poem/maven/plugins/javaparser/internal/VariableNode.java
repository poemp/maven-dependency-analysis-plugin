package org.poem.maven.plugins.javaparser.internal;

import org.poem.maven.plugins.javaparser.enums.VariableScopeEnum;

import java.util.List;

public class VariableNode extends Node{

    private String astPath;
    private String name;

    private boolean isArray;
    private VariableScopeEnum scope;

    /**
     * 原始类，若为非基本元素非Object数组：创建关系时直接指向元素类，否则不创建关系
     */
    private String origin;
    private String modifiers;
    private String assignment;

    private List<String> containerItems;


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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
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


    public List<String> getContainerItems() {
        return containerItems;
    }

    public void setContainerItems(List<String> containerItems) {
        this.containerItems = containerItems;
    }
}
