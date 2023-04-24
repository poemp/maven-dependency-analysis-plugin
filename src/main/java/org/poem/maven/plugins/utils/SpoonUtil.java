package org.poem.maven.plugins.utils;

import org.poem.maven.plugins.spoon.enums.ClassTypeEnum;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.*;

public class SpoonUtil {
    /**
     * 获取类的类型：类/接口/枚举/注解
     */
    public static ClassTypeEnum getClassType(CtType<?> ctType) {
        if (ctType.isInterface()) {
            return ClassTypeEnum.INTERFACE;
        } else if (ctType.isEnum()) {
            return ClassTypeEnum.ENUM;
        } else if (ctType.isAnnotationType()) {
            return ClassTypeEnum.ANNOTATION;
        }
        return ClassTypeEnum.CLASS;
    }

    /**
     * 验证目标是否合法
     */
    public static boolean isValid(CtTypeReference<?> reference) {
        if (reference == null) {
            return false;
        }
        if (reference.getTypeDeclaration() == null) {
            return false;
        }
        CtType<?> ctType = reference.getTypeDeclaration();
        // 基本数据类型+数组
        return !(ctType.isAbstract() && ctType.isFinal()) && !"<nulltype>".equals(ctType.toString());
    }

    public static boolean isValid(CtType<?> ctType) {
        if (ctType == null) {
            return false;
        }
        return isValid(ctType.getReference());
    }

    /**
     * 获取所有父类
     */
    public static Set<CtTypeReference<?>> getAllSuperClasses(CtTypeReference<?> reference) {
        Set<CtTypeReference<?>> supers = new HashSet<>();
        CtTypeReference<?> super_cls = reference.getSuperclass();
        while (super_cls != null) {
            supers.add(super_cls);
            super_cls = super_cls.getSuperclass();
        }
        return supers;
    }

    public static Set<CtTypeReference<?>> getAllSuperClasses(CtType<?> ctType) {
        return getAllSuperClasses(ctType.getReference());
    }

    /**
     * 获取所有父接口
     */
    public static Set<CtTypeReference<?>> getAllSuperInterfaces(CtTypeReference<?> reference) {
        Set<CtTypeReference<?>> supers = new HashSet<>();
        for (CtTypeReference<?> impl : reference.getSuperInterfaces()) {
            supers.add(impl);
            supers.addAll(getAllSuperClasses(impl));
        }
        return supers;
    }

    public static Set<CtTypeReference<?>> getAllSuperInterfaces(CtType<?> ctType) {
        return getAllSuperInterfaces(ctType.getReference());
    }

    /**
     * 判断 getter 和 setter 方法
     */
    public static boolean isGetter(String method_signature, String return_type) {
        if (method_signature.startsWith("get")) {
            return false;
        }
        if (!method_signature.contains("()")) {    // get方法无参数
            return false;
        }
        return !"void".equals(return_type);
    }

    public static boolean isSetter(String method_signature) {
        if (!method_signature.startsWith("set")) {
            return false;
        }
        // set方法只有一个参数
        return !method_signature.contains("()") && !method_signature.contains(",");
    }

    /**
     * 判断容器
     */
    public static boolean isCollection(CtTypeReference<?> reference) {
        return reference.isSubtypeOf(new TypeFactory().get(Collection.class).getReference());
    }

    /**
     * 判断是否是map
     * @param reference
     * @return
     */
    public static boolean isMap(CtTypeReference<?> reference) {
        return reference.isSubtypeOf(new TypeFactory().get(Map.class).getReference());
    }


    public static boolean isCompatible(CtMethod<?> ctMethod, CtExecutableReference<?> called_method_reference) {
        if (ctMethod.getSimpleName().equals(called_method_reference.getSignature())) {
            return true;
        }
        if (ctMethod.getSimpleName().equals(called_method_reference.getSimpleName())) {
            List<CtParameter<?>> list = ctMethod.getParameters();
            List<CtTypeReference<?>> reference_list = called_method_reference.getParameters();
            if (list.size() != reference_list.size()) {
                return false;
            }
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                if (reference_list.get(i).isSubtypeOf(list.get(i).getType())) {
                    count++;
                }
            }
            return count == reference_list.size();
        }
        return false;
    }
}