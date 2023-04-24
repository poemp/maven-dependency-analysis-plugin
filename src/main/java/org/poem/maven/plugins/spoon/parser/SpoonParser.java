package org.poem.maven.plugins.spoon.parser;


import org.poem.maven.plugins.spoon.enums.CallerTypeEnum;
import org.poem.maven.plugins.spoon.enums.RelTypeEnum;
import org.poem.maven.plugins.spoon.enums.VariableScopeEnum;
import org.poem.maven.plugins.spoon.relation.RelationEntity;
import org.poem.maven.plugins.spoon.structure.*;
import org.poem.maven.plugins.structure.*;
import org.poem.maven.plugins.utils.SpoonUtil;
import spoon.FluentLauncher;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SPOON 解析器
 *
 * @author poem
 */
public class SpoonParser {
    private static final SpoonParser instance = new SpoonParser();
    private static final EntityFactory factory = EntityFactoryImpl.getInstance();
    private static CtElement root;

    /**
     * 记录所有的引用类
     */
    private static final Set<CtTypeReference<?>> reference_set = new HashSet<>();
    /**
     * 记录已创建的类实体
     */
    private static final Set<ClassEntity> classEntities = new HashSet<>();
    /**
     * 记录已创建的方法/构造器实体
     */
    private static final Set<ExecutableEntity> executableEntities = new HashSet<>();

    public static SpoonParser getInstance() {
        return instance;
    }

    /**
     * 解析项目模块并记录实体类
     *
     * @param model 项目模块对应的 CtModel
     */
    public void parse(CtModel model) {
        model.getAllTypes().forEach(ctType -> reference_set.addAll(ctType.getReferencedTypes()));
        {
            Set<String> unFound_set = new HashSet<>();
            for (CtTypeReference<?> reference : reference_set) {
                if (!unFound_set.isEmpty()) {
                    continue;
                    //throw new ParseException("以下依赖未找到：\n" + unFound_set.toString());
                }
                if (reference.getTypeDeclaration() == null && !reference.toStringDebug().equals("<nulltype>")) {
                    unFound_set.add(reference.getQualifiedName());
                } else {
                    // 创建类节点
                    CtType<?> ctType = reference.getTypeDeclaration();
                    if (SpoonUtil.isValid(ctType) && !ctType.isPrimitive()) {
                        ClassEntity classEntity = factory.createClassEntity(ctType);
                        classEntities.add(classEntity);
                        if (SpoonUtil.isValid(ctType) && !ctType.isShadow()) {
                            ctType.getMethods().forEach(ctMethod -> {
                                MethodEntity methodEntity = factory.createMethodEntity(ctMethod);
                                executableEntities.add(methodEntity);
                                factory.createRelationEntity(classEntity, methodEntity, RelTypeEnum.HAS_METHOD);
                            });
                            if (ctType.isClass() || ctType.isEnum()) {
                                ((CtClass<?>) ctType).getConstructors().forEach(ctConstructor -> {
                                    ConstructorEntity constructorEntity = factory.createConstructorEntity(ctConstructor);
                                    executableEntities.add(constructorEntity);
                                    factory.createRelationEntity(classEntity, constructorEntity, RelTypeEnum.HAS_CONSTRUCTOR);
                                });
                            }
                        }
                    }
                }
            }
        }
        // 解析类关系
        classEntities.forEach(classEntity -> {
            if (!classEntity.isShadow()) {
                // 有超类则创建超类节点，并建立继承关系（排除 shadow 类型）
                parseSuperClass(classEntity, classEntity.getSuper_class());
                // 有超接口则创建超接口节点，并建立实现关系（排除 shadow 类型）
                parseSuperInterfaces(classEntity, classEntity.getSuper_interfaces());
                // 解析泛型，类型参数被声明于类，并建立类型参数的继承关系
                parseTypeParameter(classEntity, classEntity.getType_parameters());
                // 成员变量，被声明于类
                classEntity.getFields().forEach(ctField -> {
                    VariableEntity field_entity = factory.createVariableEntity(ctField, VariableScopeEnum.FIELD);
                    factory.createRelationEntity(classEntity, field_entity, RelTypeEnum.HAS_FIELD);
                    parseVariableEntityAssociation(field_entity);
                    {   // 初始化关系
                        CtExpression<?> assignment = field_entity.getAssignment();
                        if (assignment instanceof CtInvocation) {
                            InvocationEntity invocationEntity = factory.createInvocationEntity((CtInvocation<?>) assignment);
                            parseInvocation(field_entity, invocationEntity);
                        } else if (assignment instanceof CtConstructorCall) {
                            parseConstructorCall(field_entity, factory.createConstructorCallEntity((CtConstructorCall<?>) assignment));
                        }
                    }
                });
            }
        });
        // 解析方法和构造器
        executableEntities.forEach(executableEntity -> {
            executableEntity.getParameters().forEach(ctParameter -> {
                VariableEntity parameter_entity = factory.createVariableEntity(ctParameter, VariableScopeEnum.PARAMETER);
                factory.createRelationEntity(executableEntity, parameter_entity, RelTypeEnum.HAS_PARAMETER);
                parseVariableEntityAssociation(parameter_entity);
            });
            executableEntity.getLocal_variables().forEach(ctLocalVariable -> {
                VariableEntity local_variable_entity = factory.createVariableEntity(ctLocalVariable, VariableScopeEnum.LOCAL);
                factory.createRelationEntity(executableEntity, local_variable_entity, RelTypeEnum.HAS_LOCAL_VARIABLE);
                parseVariableEntityAssociation(local_variable_entity);
            });
            executableEntity.getInvocations().forEach(ctAbstractInvocation -> {
                if (!ctAbstractInvocation.toString().startsWith("super(") && !ctAbstractInvocation.toString().startsWith("this(")) {
                    if (ctAbstractInvocation instanceof CtInvocation) {
                        InvocationEntity invocationEntity = factory.createInvocationEntity((CtInvocation<?>) ctAbstractInvocation);
                        parseInvocation(executableEntity, invocationEntity);
                    } else if (ctAbstractInvocation instanceof CtConstructorCall) {
                        ConstructorCallEntity constructorCallEntity = factory.createConstructorCallEntity((CtConstructorCall<?>) ctAbstractInvocation);
                        parseConstructorCall(executableEntity, constructorCallEntity);
                    }
                }
            });
            if (executableEntity instanceof MethodEntity) {
                CtTypeReference<?> return_type = ((MethodEntity) executableEntity).getReturn_type();
                if (SpoonUtil.isValid(return_type) && !return_type.isShadow()) {
                    {
                        boolean isArray = return_type.isArray();
                        if (isArray) {
                            return_type = ((CtArrayTypeReference<?>) return_type).getArrayType();
                        }
                        ClassEntity return_class_entity = factory.createClassEntity(return_type.getTypeDeclaration());
                        RelationEntity relationEntity = factory.createRelationEntity(executableEntity, return_class_entity, RelTypeEnum.METHOD_RETURN);
                        if (isArray) {
                            factory.addRelationProperty(relationEntity, "Association_Type", "Array_Aggregation");
                        }
                    }
                    {
                        CtTypeReference<?> actual_return_type = ((MethodEntity) executableEntity).getActual_return_type();
                        if (SpoonUtil.isValid(actual_return_type) && !actual_return_type.isShadow() && !actual_return_type.equals(return_type)) {
                            boolean isArray = actual_return_type.isArray();
                            if (isArray) {
                                actual_return_type = ((CtArrayTypeReference<?>) actual_return_type).getArrayType();
                            }
                            ClassEntity actual_return_class_entity = factory.createClassEntity(actual_return_type.getTypeDeclaration());
                            RelationEntity relationEntity = factory.createRelationEntity(executableEntity, actual_return_class_entity, RelTypeEnum.METHOD_ACTUAL_RETURN);
                            if (isArray) {
                                factory.addRelationProperty(relationEntity, "Association_Type", "Array_Aggregation");
                            }
                        }
                    }
                }
                parseTypeParameter(executableEntity, ((MethodEntity) executableEntity).getType_parameters());
            }
        });
    }

    /**
     * 简单打印解析结果
     */
    public void printResult() {
        System.out.println("类实体：");
        getClassEntities().forEach(entity -> System.out.println(entity.getAstPath()));
        System.out.println();
        System.out.println("方法/构造器实体：");
        getExecutableEntities().forEach(entity -> System.out.println(entity.getAstPath()));
        System.out.println();
        System.out.println("变量实体：");
        getVariableEntities().forEach(entity -> System.out.println(entity.getAstPath()));
        System.out.println();
        System.out.println("调用点实体：");
        getAbstractInvocationEntities().forEach(entity -> System.out.println(entity.getAstPath()));
        System.out.println();
        System.out.println("类型变量实体：");
        getTypeParameterEntities().forEach(entity -> System.out.println(entity.getAstPath()));
        System.out.println();
        System.out.println("关系实体：");
        getRelationEntities().forEach(entity -> System.out.println(entity.getSource().getAstPath() + " : " + entity.getTarget().getAstPath() + " -> " + entity.getRelation_type()));
    }

    public CtElement getRoot() {
        return root;
    }

    /**
     * 普通项目分析器，可手动追加依赖库
     *
     * @param module_dir    项目模块路径
     * @param compile_level 目标源码的编译级别
     * @param libs          需手动添加的依赖库
     * @return CtModel 表示一个项目模块
     */
    public CtModel getFluentModel(String module_dir, int compile_level, String... libs) {
        CtModel model = new FluentLauncher()    // 快速启动器
                .inputResource(module_dir)    // 传目录，可以是模块目录也可以是项目目录
                .complianceLevel(compile_level)     // 设置目标源码的编译级别
                .sourceClassPath(libs)  // 手动添加依赖库：.jar or directory
                .buildModel();
        root = model.getRootPackage();
        return model;
    }

    /**
     * Maven 项目分析器，它会自动从 pom.xml 文件中推断源文件夹列表和依赖项，支持多模块项目分析（项目根目录下必须有pom文件说明模块间包含关系）
     *
     * @param module_dir 项目模块路径
     * @return CtModel 表示一个项目模块
     */
    public CtModel getMavenModel(String module_dir) {
        // 可选传入目录 MavenLauncher.SOURCE_TYPE：APP_SOURCE 源码目录 or TEST_SOURCE 测试目录 or ALL_SOURCE 全部
        MavenLauncher launcher = new MavenLauncher(module_dir, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        CtModel model = launcher.buildModel();
        root = model.getRootPackage();
        return model;
    }

    private void parseAbstractInvocation(BaseEntity parent_entity, AbstractInvocationEntity entity) {
        CtInvocation<?> next = entity.getNext();
        if (next != null) {
            InvocationEntity invocationEntity = factory.createInvocationEntity(next);
            parseInvocation(parent_entity, invocationEntity);
            factory.createRelationEntity(entity, invocationEntity, RelTypeEnum.NEXT_IN_CHAIN);
            parseAbstractInvocation(parent_entity, invocationEntity);
        }
    }

    private void parseInvocation(BaseEntity parent_entity, InvocationEntity invocationEntity) {
        factory.createRelationEntity(parent_entity, invocationEntity, RelTypeEnum.HAS_INVOKE);
        parseAbstractInvocation(parent_entity, invocationEntity);
        CtExecutableReference<?> called_method_reference = invocationEntity.getCalled_executable();
        if (called_method_reference.getExecutableDeclaration() instanceof CtMethod) {
            CtMethod<?> called_method = (CtMethod<?>) called_method_reference.getExecutableDeclaration();
            if (called_method == null && invocationEntity.getCalled_class() != null) {
                for (CtMethod<?> ctMethod : invocationEntity.getCalled_class().getTypeDeclaration().getAllMethods()) {
                    if (SpoonUtil.isCompatible(ctMethod, called_method_reference)) {
                        called_method = ctMethod;
                        break;
                    }
                }
            }
            if (called_method != null) {
                MethodEntity calledMethodEntity = factory.createMethodEntity(called_method);
                factory.createRelationEntity(invocationEntity, calledMethodEntity, RelTypeEnum.CALLED);
                ClassEntity calledClassEntity = factory.createClassEntity(calledMethodEntity.getDeclaration_class());
                factory.createRelationEntity(calledClassEntity, calledMethodEntity, RelTypeEnum.HAS_METHOD);

                if (CallerTypeEnum.OBJECT.equals(invocationEntity.getCallerType())) {
                    CtVariable<?> caller_obj = (CtVariable<?>) invocationEntity.getCaller_obj();
                    VariableEntity variableEntity = null;
                    if (caller_obj instanceof CtField) {
                        VariableScopeEnum scope = VariableScopeEnum.FIELD;
                        variableEntity = factory.createVariableEntity(caller_obj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        if (!(parent_entity instanceof ClassEntity)) {
                            parent_entity = factory.createClassEntity((CtType<?>) caller_obj.getParent());
                        }
                        factory.createRelationEntity(parent_entity, variableEntity, RelTypeEnum.HAS_FIELD);
                    } else if (caller_obj instanceof CtLocalVariable) {
                        VariableScopeEnum scope = VariableScopeEnum.LOCAL;
                        variableEntity = factory.createVariableEntity(caller_obj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        factory.createRelationEntity(parent_entity, variableEntity, RelTypeEnum.HAS_LOCAL_VARIABLE);
                    } else if (caller_obj instanceof CtParameter) {
                        VariableScopeEnum scope = VariableScopeEnum.PARAMETER;
                        variableEntity = factory.createVariableEntity(caller_obj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        if (parent_entity instanceof ExecutableEntity) {
                            factory.createRelationEntity(parent_entity, variableEntity, RelTypeEnum.HAS_PARAMETER);
                        }
                    } else if (caller_obj instanceof CtCatchVariable) {
                        VariableScopeEnum scope = VariableScopeEnum.CATCH;
                        variableEntity = factory.createVariableEntity(caller_obj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        factory.createRelationEntity(parent_entity, variableEntity, RelTypeEnum.HAS_EXCEPTION);
                    }
                    parseVariableEntityAssociation(variableEntity);
                }
            }
        }
    }

    private void parseConstructorCall(BaseEntity parent_entity, ConstructorCallEntity constructorCallEntity) {
        factory.createRelationEntity(parent_entity, constructorCallEntity, RelTypeEnum.HAS_INVOKE);
        parseAbstractInvocation(parent_entity, constructorCallEntity);

        CtConstructor<?> called_constructor = (CtConstructor<?>) constructorCallEntity.getCalled_executable().getExecutableDeclaration();
        ConstructorEntity constructorEntity = factory.createConstructorEntity(called_constructor);
        factory.createRelationEntity(constructorCallEntity, constructorEntity, RelTypeEnum.CALLED);

        ClassEntity classEntity = factory.createClassEntity(constructorEntity.getDeclaration_class());
        factory.createRelationEntity(classEntity, constructorEntity, RelTypeEnum.HAS_CONSTRUCTOR);
    }

    private void parseVariableEntityAssociation(VariableEntity variableEntity) {
        if (variableEntity != null) {
            CtTypeReference<?> origin = variableEntity.getOrigin();
            if (!origin.isPrimitive()) {
                ClassEntity origin_entity = factory.createClassEntity(origin.getTypeDeclaration());
                RelationEntity relationEntity = factory.createRelationEntity(variableEntity, origin_entity, RelTypeEnum.ASSOCIATION_TO);
                if (variableEntity.isArray()) {
                    factory.addRelationProperty(relationEntity, "Association_Type", "Array_Aggregation");
                } else if (variableEntity.getContainer_items() != null && !variableEntity.getContainer_items().isEmpty()) {
                    factory.addRelationProperty(relationEntity, "Association_Type", "Generic_Aggregation");
                }
                CtTypeReference<?> reference = origin_entity.getSuper_class();
                if (reference != null) {
                    ClassEntity super_cls_entity = factory.getClassEntityByQualifiedName(reference.getQualifiedName());
                    if (super_cls_entity != null) {
                        factory.createRelationEntity(origin_entity, super_cls_entity, RelTypeEnum.EXTENDS);
                    }
                }
            }
        }
    }

    private void parseTypeParameter(BaseEntity parent, List<CtTypeParameter> typeParameters) {
        typeParameters.forEach(typeParameter -> {
            TypeParameterEntity typeParameterEntity = factory.createTypeParameterEntity(typeParameter);
            factory.createRelationEntity(parent, typeParameterEntity, RelTypeEnum.HAS_GENERIC);
            parseSuperClass(typeParameterEntity, typeParameterEntity.getSuper_class());
            parseSuperInterfaces(typeParameterEntity, typeParameterEntity.getSuper_interfaces());
        });
    }

    private void parseSuperInterfaces(BaseEntity son_entity, Set<CtTypeReference<?>> super_interfaces) {
        super_interfaces.forEach(super_class -> {
            if (SpoonUtil.isValid(super_class) && !super_class.isShadow()) {
                ClassEntity super_classEntity = factory.createClassEntity(super_class.getTypeDeclaration());
                factory.createRelationEntity(son_entity, super_classEntity, RelTypeEnum.IMPLEMENTS);
            }
        });
    }

    private void parseSuperClass(BaseEntity son_entity, CtTypeReference<?> super_class) {
        if (SpoonUtil.isValid(super_class) && !super_class.isShadow()) {
            ClassEntity super_classEntity = factory.createClassEntity(super_class.getTypeDeclaration());
            factory.createRelationEntity(son_entity, super_classEntity, RelTypeEnum.EXTENDS);
        }
    }

    public Set<ClassEntity> getClassEntities() {
        return EntityFactory.classEntity_set;
    }

    public Set<ExecutableEntity> getExecutableEntities() {
        return EntityFactory.executableEntity_set;
    }

    public Set<VariableEntity> getVariableEntities() {
        return EntityFactory.variableEntity_set;
    }

    public Set<AbstractInvocationEntity> getAbstractInvocationEntities() {
        return EntityFactory.abstractInvocationEntity_set;
    }

    public Set<TypeParameterEntity> getTypeParameterEntities() {
        return EntityFactory.typeParameterEntity_set;
    }

    public Set<RelationEntity> getRelationEntities() {
        return EntityFactory.relationship_set;
    }
}
