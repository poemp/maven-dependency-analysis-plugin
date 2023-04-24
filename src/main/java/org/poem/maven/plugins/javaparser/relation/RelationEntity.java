package org.poem.maven.plugins.javaparser.relation;



import org.poem.maven.plugins.javaparser.enums.RelTypeEnum;
import org.poem.maven.plugins.javaparser.structure.BaseEntity;

import java.util.Map;
import java.util.Objects;

/**
 * 关系实体
 *
 * @author poem
 */
public class RelationEntity {
    private BaseEntity source;
    private BaseEntity target;
    private RelTypeEnum relationType;
    private Map<String, String> properties;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelationEntity that = (RelationEntity) o;
        return Objects.equals(source, that.source) &&
                Objects.equals(target, that.target) &&
                relationType == that.relationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, relationType);
    }

    public BaseEntity getSource() {
        return source;
    }

    public void setSource(BaseEntity source) {
        this.source = source;
    }

    public BaseEntity getTarget() {
        return target;
    }

    public void setTarget(BaseEntity target) {
        this.target = target;
    }

    public RelTypeEnum getRelationType() {
        return relationType;
    }

    public void setRelationType(RelTypeEnum relationType) {
        this.relationType = relationType;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "RelationEntity{\n\t" + "\n\t\"source\":" +
                source +
                ",\n\t\"target\":" +
                target +
                ",\n\t\"relation_type\":" +
                relationType +
                ",\n\t\"properties\":" +
                properties +
                "\n}";
    }
}
