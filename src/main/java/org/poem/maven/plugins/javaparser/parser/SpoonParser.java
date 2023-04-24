package org.poem.maven.plugins.javaparser.parser;


import org.poem.maven.plugins.javaparser.enums.CallerTypeEnum;
import org.poem.maven.plugins.javaparser.enums.RelTypeEnum;
import org.poem.maven.plugins.javaparser.enums.VariableScopeEnum;
import org.poem.maven.plugins.javaparser.relation.RelationEntity;
import org.poem.maven.plugins.javaparser.structure.*;
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
    private static final SpoonParser SPOON_PARSER = new SpoonParser();
    private static final EntityFactory factory = EntityFactoryImpl.getInstance();

    public static SpoonParser getInstance() {
        return SPOON_PARSER;
    }

    private static CtElement root;
    /**
     * 记录所有的引用类
     */
    private static final Set<CtTypeReference<?>> REFERENCE_SET = new HashSet<>();
    /**
     * 记录已创建的类实体
     */
    private static final Set<ClassEntity> classEntities = new HashSet<>();
    /**
     * 记录已创建的方法/构造器实体
     */
    private static final Set<ExecutableEntity> executableEntities = new HashSet<>();

    public static SpoonParser getSpoonParser() {
        return SPOON_PARSER;
    }

    /**
     * 普通项目分析器，可手动追加依赖库
     *
     * @param resourceDir  项目模块路径
     * @param compileLevel 目标源码的编译级别
     * @param libs         需手动添加的依赖库
     * @return CtModel 表示一个项目模块
     */
    public CtModel getFluentModel(String resourceDir, int compileLevel, String... libs) {
        CtModel model = new FluentLauncher()    // 快速启动器
                .inputResource(resourceDir)    // 传目录，可以是模块目录也可以是项目目录
                .complianceLevel(compileLevel)     // 设置目标源码的编译级别
                .sourceClassPath(libs)  // 手动添加依赖库：.jar or directory
                .buildModel();
        root = model.getRootPackage();
        return model;
    }

    /**
     * Maven 项目分析器，它会自动从 pom.xml 文件中推断源文件夹列表和依赖项，支持多模块项目分析（项目根目录下必须有pom文件说明模块间包含关系）
     *
     * @param moduleDir 项目模块路径
     * @return CtModel 表示一个项目模块
     */
    public CtModel getMavenModel(String moduleDir) {
        // 可选传入目录 MavenLauncher.SOURCE_TYPE：APP_SOURCE 源码目录 or TEST_SOURCE 测试目录 or ALL_SOURCE 全部
        MavenLauncher launcher = new MavenLauncher(moduleDir, MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        CtModel model = launcher.buildModel();
        root = model.getRootPackage();
        return model;
    }

    /**
     * 解析项目模块并记录实体类
     *
     * @param model 项目模块对应的 CtModel
     */
    public void parse(CtModel model) {
        model.getAllTypes().forEach(ctType -> REFERENCE_SET.addAll(ctType.getReferencedTypes()));
        {
            Set<String> unfoundSet = new HashSet<>();
            for (CtTypeReference<?> reference : REFERENCE_SET) {
                if (!unfoundSet.isEmpty()) {
                    continue;
                    //throw new ParseException("以下依赖未找到：\n" + unfoundSet.toString());
                }
                if (reference.getTypeDeclaration() == null && !reference.toStringDebug().equals("<nulltype>")) {
                    unfoundSet.add(reference.getQualifiedName());
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
                parseSuperClass(classEntity, classEntity.getSuperClass());
                // 有超接口则创建超接口节点，并建立实现关系（排除 shadow 类型）
                parseSuperInterfaces(classEntity, classEntity.getSuperInterfaces());
                // 解析泛型，类型参数被声明于类，并建立类型参数的继承关系
                parseTypeParameter(classEntity, classEntity.getTypeParameters());
                // 成员变量，被声明于类
                classEntity.getFields().forEach(ctField -> {
                    VariableEntity fieldEntity = factory.createVariableEntity(ctField, VariableScopeEnum.FIELD);
                    factory.createRelationEntity(classEntity, fieldEntity, RelTypeEnum.HAS_FIELD);
                    parseVariableEntityAssociation(fieldEntity);
                    {   // 初始化关系
                        CtExpression<?> assignment = fieldEntity.getAssignment();
                        if (assignment instanceof CtInvocation) {
                            InvocationEntity invocationEntity = factory.createInvocationEntity((CtInvocation<?>) assignment);
                            parseInvocation(fieldEntity, invocationEntity);
                        } else if (assignment instanceof CtConstructorCall) {
                            parseConstructorCall(fieldEntity, factory.createConstructorCallEntity((CtConstructorCall<?>) assignment));
                        }
                    }
                });
            }
        });
        // 解析方法和构造器
        executableEntities.forEach(executableEntity -> {
            executableEntity.getParameters().forEach(ctParameter -> {
                VariableEntity parameterEntity = factory.createVariableEntity(ctParameter, VariableScopeEnum.PARAMETER);
                factory.createRelationEntity(executableEntity, parameterEntity, RelTypeEnum.HAS_PARAMETER);
                parseVariableEntityAssociation(parameterEntity);
            });
            executableEntity.getLocalVariables().forEach(ctLocalVariable -> {
                VariableEntity localVariableEntity = factory.createVariableEntity(ctLocalVariable, VariableScopeEnum.LOCAL);
                factory.createRelationEntity(executableEntity, localVariableEntity, RelTypeEnum.HAS_LOCAL_VARIABLE);
                parseVariableEntityAssociation(localVariableEntity);
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
                CtTypeReference<?> returnType = ((MethodEntity) executableEntity).getReturn_type();
                if (SpoonUtil.isValid(returnType) && !returnType.isShadow()) {
                    {
                        boolean isArray = returnType.isArray();
                        if (isArray) {
                            returnType = ((CtArrayTypeReference<?>) returnType).getArrayType();
                        }
                        ClassEntity returnClassEntity = factory.createClassEntity(returnType.getTypeDeclaration());
                        RelationEntity relationEntity = factory.createRelationEntity(executableEntity, returnClassEntity, RelTypeEnum.METHOD_RETURN);
                        if (isArray) {
                            factory.addRelationProperty(relationEntity, "Association_Type", "Array_Aggregation");
                        }
                    }
                    {
                        CtTypeReference<?> actualReturnType = ((MethodEntity) executableEntity).getActual_return_type();
                        if (SpoonUtil.isValid(actualReturnType) && !actualReturnType.isShadow() && !actualReturnType.equals(returnType)) {
                            boolean isArray = actualReturnType.isArray();
                            if (isArray) {
                                actualReturnType = ((CtArrayTypeReference<?>) actualReturnType).getArrayType();
                            }
                            ClassEntity actual_return_class_entity = factory.createClassEntity(actualReturnType.getTypeDeclaration());
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
     * @param baseEntity
     * @param entity
     */
    private void parseAbstractInvocation(BaseEntity baseEntity, AbstractInvocationEntity entity) {
        CtInvocation<?> next = entity.getNext();
        if (next != null) {
            InvocationEntity invocationEntity = factory.createInvocationEntity(next);
            parseInvocation(baseEntity, invocationEntity);
            factory.createRelationEntity(entity, invocationEntity, RelTypeEnum.NEXT_IN_CHAIN);
            parseAbstractInvocation(baseEntity, invocationEntity);
        }
    }

    /**
     * @param parentEntity
     * @param invocationEntity
     */
    private void parseInvocation(BaseEntity parentEntity, InvocationEntity invocationEntity) {
        factory.createRelationEntity(parentEntity, invocationEntity, RelTypeEnum.HAS_INVOKE);
        parseAbstractInvocation(parentEntity, invocationEntity);
        CtExecutableReference<?> called_method_reference = invocationEntity.getCalledExecutable();
        if (called_method_reference.getExecutableDeclaration() instanceof CtMethod) {
            CtMethod<?> called_method = (CtMethod<?>) called_method_reference.getExecutableDeclaration();
            if (called_method == null && invocationEntity.getCalledClass() != null) {
                for (CtMethod<?> ctMethod : invocationEntity.getCalledClass().getTypeDeclaration().getAllMethods()) {
                    if (SpoonUtil.isCompatible(ctMethod, called_method_reference)) {
                        called_method = ctMethod;
                        break;
                    }
                }
            }
            if (called_method != null) {
                MethodEntity calledMethodEntity = factory.createMethodEntity(called_method);
                factory.createRelationEntity(invocationEntity, calledMethodEntity, RelTypeEnum.CALLED);
                ClassEntity calledClassEntity = factory.createClassEntity(calledMethodEntity.getDeclarationClass());
                factory.createRelationEntity(calledClassEntity, calledMethodEntity, RelTypeEnum.HAS_METHOD);

                if (CallerTypeEnum.OBJECT.equals(invocationEntity.getCallerType())) {
                    CtVariable<?> callerObj = (CtVariable<?>) invocationEntity.getCallerObj();
                    VariableEntity variableEntity = null;
                    if (callerObj instanceof CtField) {
                        VariableScopeEnum scope = VariableScopeEnum.FIELD;
                        variableEntity = factory.createVariableEntity(callerObj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        if (!(parentEntity instanceof ClassEntity)) {
                            parentEntity = factory.createClassEntity((CtType<?>) callerObj.getParent());
                        }
                        factory.createRelationEntity(parentEntity, variableEntity, RelTypeEnum.HAS_FIELD);
                    } else if (callerObj instanceof CtLocalVariable) {
                        VariableScopeEnum scope = VariableScopeEnum.LOCAL;
                        variableEntity = factory.createVariableEntity(callerObj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        factory.createRelationEntity(parentEntity, variableEntity, RelTypeEnum.HAS_LOCAL_VARIABLE);
                    } else if (callerObj instanceof CtParameter) {
                        VariableScopeEnum scope = VariableScopeEnum.PARAMETER;
                        variableEntity = factory.createVariableEntity(callerObj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        if (parentEntity instanceof ExecutableEntity) {
                            factory.createRelationEntity(parentEntity, variableEntity, RelTypeEnum.HAS_PARAMETER);
                        }
                    } else if (callerObj instanceof CtCatchVariable) {
                        VariableScopeEnum scope = VariableScopeEnum.CATCH;
                        variableEntity = factory.createVariableEntity(callerObj, scope);
                        factory.createRelationEntity(invocationEntity, variableEntity, RelTypeEnum.OBJ);
                        factory.createRelationEntity(parentEntity, variableEntity, RelTypeEnum.HAS_EXCEPTION);
                    }
                    parseVariableEntityAssociation(variableEntity);
                }
            }
        }
    }

    /**
     * @param parentEntity
     * @param constructorCallEntity
     */
    private void parseConstructorCall(BaseEntity parentEntity, ConstructorCallEntity constructorCallEntity) {
        factory.createRelationEntity(parentEntity, constructorCallEntity, RelTypeEnum.HAS_INVOKE);
        parseAbstractInvocation(parentEntity, constructorCallEntity);

        CtConstructor<?> called_constructor = (CtConstructor<?>) constructorCallEntity.getCalledExecutable().getExecutableDeclaration();
        ConstructorEntity constructorEntity = factory.createConstructorEntity(called_constructor);
        factory.createRelationEntity(constructorCallEntity, constructorEntity, RelTypeEnum.CALLED);

        ClassEntity classEntity = factory.createClassEntity(constructorEntity.getDeclarationClass());
        factory.createRelationEntity(classEntity, constructorEntity, RelTypeEnum.HAS_CONSTRUCTOR);
    }

    /**
     * @param variableEntity
     */
    private void parseVariableEntityAssociation(VariableEntity variableEntity) {
        if (variableEntity != null) {
            CtTypeReference<?> origin = variableEntity.getOrigin();
            if (!origin.isPrimitive()) {
                ClassEntity origin_entity = factory.createClassEntity(origin.getTypeDeclaration());
                RelationEntity relationEntity = factory.createRelationEntity(variableEntity, origin_entity, RelTypeEnum.ASSOCIATION_TO);
                if (variableEntity.isArray()) {
                    factory.addRelationProperty(relationEntity, "Association_Type", "Array_Aggregation");
                } else if (variableEntity.getContainerItems() != null && !variableEntity.getContainerItems().isEmpty()) {
                    factory.addRelationProperty(relationEntity, "Association_Type", "Generic_Aggregation");
                }
                CtTypeReference<?> reference = origin_entity.getSuperClass();
                if (reference != null) {
                    ClassEntity super_cls_entity = factory.getClassEntityByQualifiedName(reference.getQualifiedName());
                    if (super_cls_entity != null) {
                        factory.createRelationEntity(origin_entity, super_cls_entity, RelTypeEnum.EXTENDS);
                    }
                }
            }
        }
    }

    /**
     * @param parent
     * @param typeParameters
     */
    private void parseTypeParameter(BaseEntity parent, List<CtTypeParameter> typeParameters) {
        typeParameters.forEach(typeParameter -> {
            TypeParameterEntity typeParameterEntity = factory.createTypeParameterEntity(typeParameter);
            factory.createRelationEntity(parent, typeParameterEntity, RelTypeEnum.HAS_GENERIC);
            parseSuperClass(typeParameterEntity, typeParameterEntity.getSuper_class());
            parseSuperInterfaces(typeParameterEntity, typeParameterEntity.getSuper_interfaces());
        });
    }

    /**
     * @param sonEntity
     * @param superInterfaces
     */
    private void parseSuperInterfaces(BaseEntity sonEntity, Set<CtTypeReference<?>> superInterfaces) {
        superInterfaces.forEach(super_class -> {
            if (SpoonUtil.isValid(super_class) && !super_class.isShadow()) {
                ClassEntity super_classEntity = factory.createClassEntity(super_class.getTypeDeclaration());
                factory.createRelationEntity(sonEntity, super_classEntity, RelTypeEnum.IMPLEMENTS);
            }
        });
    }

    private void parseSuperClass(BaseEntity sonEntity, CtTypeReference<?> superClass) {
        if (SpoonUtil.isValid(superClass) && !superClass.isShadow()) {
            ClassEntity super_classEntity = factory.createClassEntity(superClass.getTypeDeclaration());
            factory.createRelationEntity(sonEntity, super_classEntity, RelTypeEnum.EXTENDS);
        }
    }

    public Set<ClassEntity> getClassEntities() {
        return EntityFactory.CLASS_ENTITY_SET;
    }

    public Set<ExecutableEntity> getExecutableEntities() {
        return EntityFactory.EXECUTABLE_ENTITY_SET;
    }

    public Set<VariableEntity> getVariableEntities() {
        return EntityFactory.VARIABLE_ENTITY_SET;
    }

    public Set<AbstractInvocationEntity> getAbstractInvocationEntities() {
        return EntityFactory.ABSTRACT_INVOCATION_ENTITY_SET;
    }

    public Set<TypeParameterEntity> getTypeParameterEntities() {
        return EntityFactory.TYPE_PARAMETER_ENTITY_SET;
    }

    public Set<RelationEntity> getRelationEntities() {
        return EntityFactory.RELATIONSHIP_SET;
    }
}
