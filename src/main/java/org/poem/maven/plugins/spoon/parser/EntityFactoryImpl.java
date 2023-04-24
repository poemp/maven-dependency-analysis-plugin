package org.poem.maven.plugins.spoon.parser;


import org.poem.maven.plugins.spoon.enums.CallerTypeEnum;
import org.poem.maven.plugins.spoon.enums.EntityTypeEnum;
import org.poem.maven.plugins.spoon.enums.RelTypeEnum;
import org.poem.maven.plugins.spoon.enums.VariableScopeEnum;
import org.poem.maven.plugins.spoon.relation.RelationEntity;
import org.poem.maven.plugins.spoon.structure.*;
import org.poem.maven.plugins.structure.*;
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
            entity.setClass_type(SpoonUtil.getClassType(ctType));
            entity.setModifiers(ctType.getModifiers().toString());
            entity.setInner(ctType.getPath().toString().contains("$"));
            entity.setParameterized(ctType.isParameterized());
            entity.setShadow(ctType.isShadow());
            entity.setSuper_class(ctType.getSuperclass());
            entity.setAll_super_classes(SpoonUtil.getAllSuperClasses(ctType));
            entity.setSuper_interfaces(ctType.getSuperInterfaces());
            entity.setAll_super_interfaces(SpoonUtil.getAllSuperInterfaces(ctType));
            {
                Set<? extends CtConstructor<?>> constructors = new HashSet<>();
                if (!ctType.isInterface() && !ctType.isAnnotationType()) {
                    constructors = ((CtClass<?>) ctType).getConstructors();
                }
                entity.setConstructors(constructors);
            }
            entity.setFields(ctType.getFields());
            entity.setMethods(ctType.getMethods());
            entity.setType_parameters(ctType.getFormalCtTypeParameters());
            classEntity_set.add(entity);
        }
        return entity;
    }

    @Override
    public ConstructorEntity createConstructorEntity(CtConstructor<?> ctConstructor) {
        ConstructorEntity entity = (ConstructorEntity) getExecutableEntityBySignature(ctConstructor.getSignature());
        if (entity == null) {
            entity = new ConstructorEntity(ctConstructor.getPath().toString());
            entity.setFull_signature(ctConstructor.getSignature());
            entity.setModifiers(ctConstructor.getModifiers().toString());
            entity.setDeclaration_class(ctConstructor.getDeclaringType());
            entity.setParameters(ctConstructor.getParameters());
            entity.setLocal_variables(ctConstructor.getBody().getElements(new TypeFilter<>(CtLocalVariable.class)));
            entity.setInvocations(ctConstructor.getBody().getElements(new TypeFilter<>(CtAbstractInvocation.class)));
            executableEntity_set.add(entity);
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
            entity.setFull_signature(ctMethod.getDeclaringType().getQualifiedName() + "." + ctMethod.getSignature());
            entity.setModifiers(ctMethod.getModifiers().toString());
            entity.setDeclaration_class(ctMethod.getDeclaringType());
            entity.setParameters(ctMethod.getParameters());
            if (ctMethod.getBody() != null) {
                entity.setLocal_variables(ctMethod.getBody().getElements(new TypeFilter<>(CtLocalVariable.class)));
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
            executableEntity_set.add(entity);
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
            typeParameterEntity_set.add(entity);
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
                entity.setDeclaration_in(parent);
            }
            {
                CtExecutableReference<?> called_executable = ctInvocation.getExecutable();
                entity.setCalled_executable(called_executable);
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
                entity.setCalled_class(called_class);
            }
            entity.setArgument_types(ctInvocation.getActualTypeArguments());
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
                        entity.setCaller_obj(ctVariable);
                    } else if (ctExpression instanceof CtAbstractInvocation) {
                        entity.setCallerType(CallerTypeEnum.ANONYMOUS);
                        entity.setCaller_obj(ctExpression);
                    } else if (ctExpression instanceof CtThisAccess) {
                        entity.setCallerType(CallerTypeEnum.THIS);
                        entity.setCaller_obj(ctExpression);
                    } else if (ctExpression instanceof CtSuperAccess) {
                        entity.setCallerType(CallerTypeEnum.SUPER);
                        entity.setCaller_obj(ctExpression);
                    } else if (ctExpression instanceof CtArrayAccess) {
                        entity.setCallerType(CallerTypeEnum.ARRAY);
                        entity.setCaller_obj(ctExpression);
                    } else {
                        entity.setCallerType(CallerTypeEnum.LITERAL);
                        entity.setCaller_obj(ctExpression);
                    }
                }
            }
            abstractInvocationEntity_set.add(entity);
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
                entity.setDeclaration_in(parent);
            }
            {
                CtExecutableReference<?> called_executable = ctConstructorCall.getExecutable();
                entity.setCalled_executable(called_executable);
                entity.setCalled_class(called_executable.getDeclaringType());
            }
            entity.setArgument_types(ctConstructorCall.getActualTypeArguments());
            if ("target".equals(ctConstructorCall.getRoleInParent().getCamelCaseName())) {
                entity.setNext((CtInvocation<?>) ctConstructorCall.getParent());
            }
            abstractInvocationEntity_set.add(entity);
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
                entity.setContainer_items(list);
            }
            variableEntity_set.add(entity);
        }

        return entity;
    }

    @Override
    public ClassEntity getClassEntityByQualifiedName(String name) {
        for (ClassEntity classEntity : classEntity_set) {
            if (classEntity.getName().equals(name)) {
                return classEntity;
            }
        }
        return null;
    }

    @Override
    public ExecutableEntity getExecutableEntityBySignature(String full_signature) {
        for (ExecutableEntity executableEntity : executableEntity_set) {
            if (executableEntity.getFull_signature().equals(full_signature)) {
                return executableEntity;
            }
        }
        return null;
    }

    @Override
    public BaseEntity getEntityByAstPath(EntityTypeEnum entityType, String ast_path) {
        Set<?> search_set = classEntity_set;
        switch (entityType) {
            case InvocationEntity:
            case ConstructorCallEntity:
                search_set = abstractInvocationEntity_set;
                break;
            case MethodEntity:
            case ConstructorEntity:
                search_set = executableEntity_set;
                break;
            case VariableEntity:
                search_set = variableEntity_set;
                break;
            case TypeParameterEntity:
                search_set = typeParameterEntity_set;
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
        entity.setRelation_type(relType);
        relationship_set.add(entity);
        return entity;
    }

    @Override
    public RelationEntity createRelationEntity(BaseEntity source, BaseEntity target, RelTypeEnum relType, Map<String, String> properties) {
        RelationEntity entity = new RelationEntity();
        entity.setSource(source);
        entity.setTarget(target);
        entity.setRelation_type(relType);
        entity.setProperties(properties);
        relationship_set.add(entity);
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
