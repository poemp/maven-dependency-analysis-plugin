package org.poem.maven.plugins.javaparser.structure;

import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypeParameter;
import spoon.reflect.reference.CtTypeReference;

import java.util.List;
import java.util.Set;

/**
 * 方法实体
 *
 * @author poem
 */
public class MethodEntity extends ExecutableEntity {
    private String name;
    private String signature;
    private CtTypeReference<?> return_type;
    /**
     * 可能返回子类
     */
    private CtTypeReference<?> actual_return_type;
    private List<CtTypeParameter> type_parameters;
    private boolean isGetter;
    private boolean isSetter;
    /**
     * 重写的方法所在类，为空则为自定义的方法
     */
    private Set<CtType<?>> override_classes;

    public MethodEntity(String ast_path) {
        super(ast_path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public CtTypeReference<?> getReturn_type() {
        return return_type;
    }

    public void setReturn_type(CtTypeReference<?> return_type) {
        this.return_type = return_type;
    }

    public CtTypeReference<?> getActual_return_type() {
        return actual_return_type;
    }

    public void setActual_return_type(CtTypeReference<?> actual_return_type) {
        this.actual_return_type = actual_return_type;
    }

    public List<CtTypeParameter> getType_parameters() {
        return type_parameters;
    }

    public void setType_parameters(List<CtTypeParameter> type_parameters) {
        this.type_parameters = type_parameters;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public void setGetter(boolean getter) {
        isGetter = getter;
    }

    public boolean isSetter() {
        return isSetter;
    }

    public void setSetter(boolean setter) {
        isSetter = setter;
    }

    public Set<CtType<?>> getOverride_classes() {
        return override_classes;
    }

    public void setOverride_classes(Set<CtType<?>> override_classes) {
        this.override_classes = override_classes;
    }

    @Override
    public String toString() {
        return "MethodEntity{\n\t" + super.toString() +
                "\n\t\"name\":\"" +
                name + '\"' +
                ",\n\t\"signature\":\"" +
                signature + '\"' +
                ",\n\t\"return_type\":" +
                return_type +
                ",\n\t\"actual_return_type\":" +
                actual_return_type +
                ",\n\t\"type_parameters\":" +
                type_parameters +
                ",\n\t\"isGetter\":" +
                isGetter +
                ",\n\t\"isSetter\":" +
                isSetter +
                ",\n\t\"override_classes\":" +
                override_classes +
                "\n}";
    }
}
