package org.poem.maven.plugins.spoon.structure;

import java.util.Objects;

/**
 * 抽象实体，是所有实体的父类
 * 每个实体都保留一份在抽象语法树中的路径 ast_path，并保留相关实体的 ast_path
 *
 * @author poem
 */
public abstract class BaseEntity {
    /**
     * 在抽象语法树中的路径
     */
    private String astPath;
    private String entityType;

    public BaseEntity(String astPath) {
        this.astPath = astPath;
        this.entityType = this.getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(astPath, that.astPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(astPath);
    }

    public String getAstPath() {
        return astPath;
    }

    public void setAstPath(String ast_path) {
        this.astPath = ast_path;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String toString() {
        return "BaseEntity{\n\t" + "\n\t\"ast_path\":\"" +
                astPath + '\"' +
                ",\n\t\"entityType\":\"" +
                entityType + '\"' +
                "\n}";
    }
}
