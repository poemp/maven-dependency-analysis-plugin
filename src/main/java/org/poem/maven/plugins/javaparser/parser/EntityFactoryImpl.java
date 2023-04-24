package org.poem.maven.plugins.javaparser.parser;


import org.poem.maven.plugins.javaparser.enums.CallerTypeEnum;
import org.poem.maven.plugins.javaparser.enums.EntityTypeEnum;
import org.poem.maven.plugins.javaparser.enums.RelTypeEnum;
import org.poem.maven.plugins.javaparser.enums.VariableScopeEnum;
import org.poem.maven.plugins.javaparser.relation.RelationEntity;
import org.poem.maven.plugins.javaparser.structure.*;
import org.poem.maven.plugins.utils.SpoonUtil;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.CtVariableReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.*;

public class EntityFactoryImpl implements EntityFactory {

    private static EntityFactory factory;

    public static EntityFactory getInstance() {
        if (factory == null) {
            factory = new EntityFactoryImpl();
        }
        return factory;
    }

    @Override
    public ClassEntity createClassEntity(CtType<?> ctType) {
        ClassEntity entity = getClassEntityByQualifiedName(ctType.getQualifiedName());
        if (entity == null) {
            entity = new ClassEntity(ctType.getPath().toString());
            entity.setName(ctType.getQualifiedName());
            entity.setClassType(SpoonUtil.getClassType(ctType));
            entity.setModifiers(ctType.getModifiers().toString());
            entity.setInner(ctType.getPath().toString().contains("$"));
            entity.setParameterized(ctType.isParameterized());
            entity.setShadow(ctType.isShadow());
            entity.setSuperClass(ctType.getSuperclass());
            entity.setAllSuperClasses(SpoonUtil.getAllSuperClasses(ctType));
            entity.setSuperInterfaces(ctType.getSuperInterfaces());
            entity.setAllSuperInterfaces(SpoonUtil.getAllSuperInterfaces(ctType));
            {
                Set<? extends CtConstructor<?>> constructors = new HashSet<>();
                if (!ctType.isInterface() && !ctType.isAnnotationType()) {
                    constructors = ((CtClass<?>) ctType).getConstructors();
                }
                entity.setConstructors(constructors);
            }
            entity.setFields(ctType.getFields());
            entity.setMethods(ctType.getMethods());
            entity.setTypeParameters(ctType.getFormalCtTypeParameters());
            CLASS_ENTITY_SET.add(entity);
        }
        return entity;
    }

    @Override
    public ConstructorEntity createConstructorEntity(CtConstructor<?> ctConstructor) {
        ConstructorEntity entity = (ConstructorEntity) getExecutableEntityBySignature(ctConstructor.getSignature());
        if (entity == null) {
            entity = new ConstructorEntity(ctConstructor.getPath().toString());
            entity.setFullSignature(ctConstructor.getSignature());
            entity.setModifiers(ctConstructor.getModifiers().toString());
            entity.setDeclarationClass(ctConstructor.getDeclaringType());
            entity.setParameters(ctConstructor.getParameters());
            entity.setLocalVariables(ctConstructor.getBody().getElements(new TypeFilter<>(CtLocalVariable.class)));
            entity.setInvocations(ctConstructor.getBody().getElements(new TypeFilter<>(CtAbstractInvocation.class)));
            EXECUTABLE_ENTITY_SET.add(entity);
        }
        return entity;
    }

    @Override
    public MethodEntity createMethodEntity(CtMethod<?> ctMethod) {
        MethodEntity entity = (MethodEntity) getExecutableEntityBySignature(ctMethod.getDeclaringType().getQualifiedName() + "." + ctMethod.getSignature());
        if (entity == null) {
            entity = new MethodEntity(ctMethod.getPath().toString());
            entity.setName(ctMethod.getSimpleName());
            entity.setSignature(ctMethod.getSignature());
            entity.setGetter(SpoonUtil.isGetter(ctMethod.getSignature(), ctMethod.getType().getQualifiedName()));
            entity.setSetter(SpoonUtil.isSetter(ctMethod.getSignature()));
            entity.setFullSignature(ctMethod.getDeclaringType().getQualifiedName() + "." + ctMethod.getSignature());
            entity.setModifiers(ctMethod.getModifiers().toString());
            entity.setDeclarationClass(ctMethod.getDeclaringType());
            entity.setParameters(ctMethod.getParameters());
            if (ctMethod.getBody() != null) {
                entity.setLocalVariables(ctMethod.getBody().getElements(new TypeFilter<>(CtLocalVariable.class)));
                entity.setInvocations(ctMethod.getBody().getElements(new TypeFilter<>(CtAbstractInvocation.class)));
            }
            entity.setReturn_type(ctMethod.getType());
            entity.setType_parameters(ctMethod.getFormalCtTypeParameters());
            {
                List<CtReturn<?>> returns = ctMethod.getElements(new TypeFilter<>(CtReturn.class));
                if (returns != null && !returns.isEmpty()) {
                    CtExpression<?> actual_return_expression = returns.get(0).getReturnedExpression();
                    if (actual_return_expression != null) {
                        entity.setActual_return_type(actual_return_expression.getType());
                    }
                }
            }
            {
                Set<CtType<?>> override_classes = new HashSet<>();
                Collection<CtMethod<?>> override_methods = ctMethod.getTopDefinitions();
                override_methods.remove(ctMethod);
                if (!override_methods.isEmpty()) {
                    override_methods.forEach(override_method -> override_classes.add(override_method.getDeclaringType()));
                }
                entity.setOverride_classes(override_classes);
            }
            EXECUTABLE_ENTITY_SET.add(entity);
        }
        return entity;
    }

    @Override
    public TypeParameterEntity createTypeParameterEntity(CtTypeParameter ctTypeParameter) {
        TypeParameterEntity entity = (TypeParameterEntity) getEntityByAstPath(EntityTypeEnum.TypeParameterEntity, ctTypeParameter.getPath().toString());
        if (entity == null) {
            entity = new TypeParameterEntity(ctTypeParameter.getPath().toString());
            entity.setSuper_class(ctTypeParameter.getSuperclass());
            entity.setSuper_interfaces(ctTypeParameter.getSuperInterfaces());
            {
                CtElement parent = ctTypeParameter.getParent();
                if (parent instanceof CtMethod || parent instanceof CtTypeInformation) {
                    entity.setDeclaration_in(parent);
                }
            }
            TYPE_PARAMETER_ENTITY_SET.add(entity);
        }
        return entity;
    }

    @Override
    public InvocationEntity createInvocationEntity(CtInvocation<?> ctInvocation) {
        InvocationEntity entity = (InvocationEntity) getEntityByAstPath(EntityTypeEnum.InvocationEntity, ctInvocation.getPath().toString());
        if (entity == null) {
            entity = new InvocationEntity(ctInvocation.getPath().toString());
            entity.setCode(ctInvocation.toString());
            {
                CtElement parent = ctInvocation.getParent();
                while (!(parent instanceof CtExecutable) && !(parent instanceof CtVariable)) {
                    parent = parent.getParent();
                }
                entity.setDeclarationIn(parent);
            }
            {
                CtExecutableReference<?> called_executable = ctInvocation.getExecutable();
                entity.setCalledExecutable(called_executable);
                CtTypeReference<?> called_class = called_executable.getDeclaringType();
                if (called_class == null) {
                    CtElement parent = ctInvocation.getParent();
                    while (!(parent instanceof CtType)) {
                        parent = parent.getParent();
                    }
                    for (CtTypeReference<?> referencedType : parent.getReferencedTypes()) {
                        if (SpoonUtil.isValid(referencedType)) {
                            for (CtMethod<?> ctMethod : referencedType.getTypeDeclaration().getMethods()) {
                                if (SpoonUtil.isCompatible(ctMethod, called_executable)) {
                                    called_class = referencedType;
                                }
                            }
                        }
                    }
                }
                entity.setCalledClass(called_class);
            }
            entity.setArgumentTypes(ctInvocation.getActualTypeArguments());
            if ("target".equals(ctInvocation.getRoleInParent().getCamelCaseName()) && ctInvocation.getParent() instanceof CtInvocation) {
                entity.setNext((CtInvocation<?>) ctInvocation.getParent());
            }
            {
                CtExpression<?> ctExpression = ctInvocation.getTarget();
                if (ctExpression != null) {
                    if (ctExpression instanceof CtTypeAccess) {
                        entity.setCallerType(CallerTypeEnum.STATIC);
                    } else if (ctExpression instanceof CtVariableAccess) {
                        entity.setCallerType(CallerTypeEnum.OBJECT);
                        CtVariableReference<?> reference = ((CtVariableAccess<?>) ctExpression).getVariable();
                        CtVariable<?> ctVariable = reference.getDeclaration();
                        if (ctVariable == null) {
                            String field_qualified_name = reference.toString();
                            if (field_qualified_name.indexOf('.') > 0) {
                                String field_name = field_qualified_name.substring(field_qualified_name.lastIndexOf('.') + 1);
                                String cls_name = field_qualified_name.substring(0, field_qualified_name.lastIndexOf('.'));
                                ClassEntity cls_entity = getClassEntityByQualifiedName(cls_name);
                                if (cls_entity != null) {
                                    for (CtField<?> field : cls_entity.getFields()) {
                                        if (field.getSimpleName().equals(field_name)) {
                                            ctVariable = field;
                                        }
                                    }
                                }
                            }
                        }
                        entity.setCallerObj(ctVariable);
                    } else if (ctExpression instanceof CtAbstractInvocation) {
                        entity.setCallerType(CallerTypeEnum.ANONYMOUS);
                        entity.setCallerObj(ctExpression);
                    } else if (ctExpression instanceof CtThisAccess) {
                        entity.setCallerType(CallerTypeEnum.THIS);
                        entity.setCallerObj(ctExpression);
                    } else if (ctExpression instanceof CtSuperAccess) {
                        entity.setCallerType(CallerTypeEnum.SUPER);
                        entity.setCallerObj(ctExpression);
                    } else if (ctExpression instanceof CtArrayAccess) {
                        entity.setCallerType(CallerTypeEnum.ARRAY);
                        entity.setCallerObj(ctExpression);
                    } else {
                        entity.setCallerType(CallerTypeEnum.LITERAL);
                        entity.setCallerObj(ctExpression);
                    }
                }
            }
            ABSTRACT_INVOCATION_ENTITY_SET.add(entity);
        }
        return entity;
    }

    @Override
    public ConstructorCallEntity createConstructorCallEntity(CtConstructorCall<?> ctConstructorCall) {
        ConstructorCallEntity entity = (ConstructorCallEntity) getEntityByAstPath(EntityTypeEnum.ConstructorCallEntity, ctConstructorCall.getPath().toString());
        if (entity == null) {
            entity = new ConstructorCallEntity(ctConstructorCall.getPath().toString());
            entity.setCode(ctConstructorCall.toString());
            {
                CtElement parent = ctConstructorCall.getParent();
                while (!(parent instanceof CtExecutable) && !(parent instanceof CtVariable)) {
                    parent = parent.getParent();
                }
                entity.setDeclarationIn(parent);
            }
            {
                CtExecutableReference<?> called_executable = ctConstructorCall.getExecutable();
                entity.setCalledExecutable(called_executable);
                entity.setCalledClass(called_executable.getDeclaringType());
            }
            entity.setArgumentTypes(ctConstructorCall.getActualTypeArguments());
            if ("target".equals(ctConstructorCall.getRoleInParent().getCamelCaseName())) {
                entity.setNext((CtInvocation<?>) ctConstructorCall.getParent());
            }
            ABSTRACT_INVOCATION_ENTITY_SET.add(entity);
        }
        return entity;
    }


    @Override
    public VariableEntity createVariableEntity(CtVariable<?> ctVariable, VariableScopeEnum scope) {
        VariableEntity entity = (VariableEntity) getEntityByAstPath(EntityTypeEnum.VariableEntity, ctVariable.getPath().toString());
        if (entity == null) {
            entity = new VariableEntity(ctVariable.getPath().toString());
            entity.setScope(scope);
            entity.setName(ctVariable.getSimpleName());
            entity.setModifiers(ctVariable.getModifiers().toString());
            entity.setAssignment(ctVariable.getDefaultExpression());    // 包括 CtAbstractInvocation 和 CtLiteral 两种类型
            {
                CtTypeReference<?> origin = ctVariable.getType();
                entity.setArray(origin.isArray());
                if (origin.isArray()) {
                    origin = ((CtArrayTypeReference<?>) origin).getArrayType();
                }
                entity.setOrigin(origin);
                List<CtTypeReference<?>> list = new ArrayList<>();
                if (SpoonUtil.isCollection(origin) || SpoonUtil.isMap(origin)) {
                    list.addAll(origin.getReferencedTypes());
                    list.remove(origin);
                }
                entity.setContainerItems(list);
            }
            VARIABLE_ENTITY_SET.add(entity);
        }

        return entity;
    }

    @Override
    public ClassEntity getClassEntityByQualifiedName(String name) {
        for (ClassEntity classEntity : CLASS_ENTITY_SET) {
            if (classEntity.getName().equals(name)) {
                return classEntity;
            }
        }
        return null;
    }

    @Override
    public ExecutableEntity getExecutableEntityBySignature(String full_signature) {
        for (ExecutableEntity executableEntity : EXECUTABLE_ENTITY_SET) {
            if (executableEntity.getFullSignature().equals(full_signature)) {
                return executableEntity;
            }
        }
        return null;
    }

    @Override
    public BaseEntity getEntityByAstPath(EntityTypeEnum entityType, String ast_path) {
        Set<?> search_set = CLASS_ENTITY_SET;
        switch (entityType) {
            case InvocationEntity:
            case ConstructorCallEntity:
                search_set = ABSTRACT_INVOCATION_ENTITY_SET;
                break;
            case MethodEntity:
            case ConstructorEntity:
                search_set = EXECUTABLE_ENTITY_SET;
                break;
            case VariableEntity:
                search_set = VARIABLE_ENTITY_SET;
                break;
            case TypeParameterEntity:
                search_set = TYPE_PARAMETER_ENTITY_SET;
                break;
            default:
                break;
        }
        for (Object obj : search_set) {
            BaseEntity entity = (BaseEntity) obj;
            if (entity.getAstPath().equals(ast_path)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public RelationEntity createRelationEntity(BaseEntity source, BaseEntity target, RelTypeEnum relType) {
        RelationEntity entity = new RelationEntity();
        entity.setSource(source);
        entity.setTarget(target);
        entity.setRelationType(relType);
        RELATIONSHIP_SET.add(entity);
        return entity;
    }

    @Override
    public RelationEntity createRelationEntity(BaseEntity source, BaseEntity target, RelTypeEnum relType, Map<String, String> properties) {
        RelationEntity entity = new RelationEntity();
        entity.setSource(source);
        entity.setTarget(target);
        entity.setRelationType(relType);
        entity.setProperties(properties);
        RELATIONSHIP_SET.add(entity);
        return entity;
    }

    @Override
    public RelationEntity addRelationProperty(RelationEntity entity, String key, String val) {
        Map<String, String> properties = entity.getProperties();
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, val);
        entity.setProperties(properties);
        return entity;
    }

}
