package com.raybritton.autovaluemap;

import com.google.auto.service.AutoService;
import com.google.auto.value.extension.AutoValueExtension;
import com.google.common.collect.Lists;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import static com.raybritton.autovaluemap.MapElement.Type.ADAPTER;
import static com.raybritton.autovaluemap.MapElement.Type.ENUM;
import static com.raybritton.autovaluemap.MapElement.Type.HIDDEN;

/**
 * This AutoValue extension creates methods to convert to and from Map&lt;String, Object&gt; and a AutoValue model
 *
 * By default only primitives, boxed primitives, primitive arrays, Strings and enums (via @MapEnum) are supported.
 * However any serialisable object can be supported via an adapter.
 *
 * Methods added:
 * Map&lt;String, Object&gt; toMap()
 * This returns a map of the object
 * static M fromMap(Map&lt;String, Object&gt;)
 * This returns an object from the map
 *
 * This library supports the following annotations for methods
 * MapElementName
 * This changes the name used in the map, by default the methods name will be used
 * MapHide
 * This stops the value from the method being set in the map and when creating the object
 * null be set.
 * This can be changed to any value by setting a {@link MapValueMaker} and/or by using the
 * value from the map by setting readFromMap to true
 * MapElementAdapter
 * By default the value from the method will be set in the map, if this is an object or
 * some other value that needs to be serialised then an {@link MapAdapter} will convert to and from
 * the map and method value.
 * MapEnum
 * This will automatically handle serialising enums and must be set on enums.
 *
 */
@AutoService(AutoValueExtension.class)
public class AutoValueMapExtension extends AutoValueExtension {

    private static final TypeName ACTUAL_MAP_TYPE = ParameterizedTypeName.get(HashMap.class, String.class, Object.class);
    private static final TypeName RETURN_MAP_TYPE = ParameterizedTypeName.get(Map.class, String.class, Object.class);
    private static final TypeName PARAM_MAP_TYPE = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    @Override
    public String generateClass(Context context, String className, String classToExtend, boolean isFinal) {
        List<MapElement> valueMethods = new ArrayList<>();

        for (ExecutableElement element : context.abstractMethods()) {
            MapElement item = MapElement.create(context, element);
            if (item != null) {
                valueMethods.add(item);
            }
        }

        ClassName superclass = ClassName.get(context.packageName(), classToExtend);
        ClassName trueClass = ClassName.get(context.packageName(), className.substring(className.indexOf('_') + 1));
        TypeSpec.Builder subclass = TypeSpec.classBuilder(className)
                .addModifiers(isFinal ? Modifier.FINAL : Modifier.ABSTRACT)
                .addMethod(generateConstructor(valueMethods))
                .addMethod(generateToMap(valueMethods))
                .addMethod(generateFromMap(context, trueClass, valueMethods));

        subclass.superclass(superclass);

        JavaFile file = JavaFile.builder(context.packageName(), subclass.build()).build();

        return file.toString();
    }

    @Override
    public boolean applicable(Context context) {
        for (ExecutableElement element : context.abstractMethods()) {
            if (element.getSimpleName().toString().equals("toMap")) {
                return true;
            }
        }
        return false;
    }

    private MethodSpec generateToMap(Collection<MapElement> valueMethods) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toMap")
                .addModifiers(Modifier.PUBLIC)
                .returns(RETURN_MAP_TYPE);

        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        codeBuilder.addStatement("$T map = new $T()", PARAM_MAP_TYPE, ACTUAL_MAP_TYPE);
        for (MapElement element : valueMethods) {
            if (element.getPropertyType() == HIDDEN) {
                //do nothing
            } else if (element.getPropertyType() == ADAPTER) {
                codeBuilder.beginControlFlow("try");
                codeBuilder.addStatement("$T adapter = ($T) $T.class.newInstance()", MapAdapter.class, MapAdapter.class, element.getAdapter());
                codeBuilder.addStatement("map.put($S, ($T) adapter.toMap($S, $N()))", element.getMapItemName(), element.getAdapterType(), element.getMethodName(), element.getMethodName());
                codeBuilder.nextControlFlow("catch (InstantiationException | IllegalAccessException e) ");
                codeBuilder.addStatement("throw new IllegalStateException(\"Cannot instantiate adapter for $L because \"+e.getMessage())", element.getMethodName());
                codeBuilder.endControlFlow();
            } else if (element.getPropertyType() == ENUM) {
                codeBuilder.beginControlFlow("if ($N() != null)", element.getMethodName());
                codeBuilder.addStatement("map.put($S, $N().name())", element.getMapItemName(), element.getMethodName());
                codeBuilder.endControlFlow();
            } else {
                codeBuilder.addStatement("map.put($S, $N())", element.getMapItemName(), element.getMethodName());
            }
        }
        codeBuilder.addStatement("return map");

        builder.addCode(codeBuilder.build());
        return builder.build();
    }

    private MethodSpec generateFromMap(Context context, ClassName superclass, Collection<MapElement> valueMethods) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("fromMap")
                .addModifiers(Modifier.STATIC)
                .addModifiers(Modifier.PUBLIC)
                .returns(superclass)
                .addParameter(PARAM_MAP_TYPE, "map");

        String[] names = new String[valueMethods.size()];
        int idx = 0;

        CodeBlock.Builder codeBuilder = CodeBlock.builder();

        for (MapElement element : valueMethods) {
            names[idx] = element.getMethodName();
            idx++;
            if (element.getPropertyType() == HIDDEN) {
                codeBuilder.addStatement("$T $N", element.getReturnType(), element.getMethodName());
                codeBuilder.beginControlFlow("try");
                if (element.isReadFromMap()) {
                    codeBuilder.beginControlFlow("if (map.containsKey($S))", element.getMapItemName());
                    codeBuilder.addStatement("$N = ($T) map.get($S)", element.getMethodName(), element.getReturnType(), element.getMapItemName());
                    codeBuilder.nextControlFlow("else");
                    String makerName = element.getMethodName() + "Maker";
                    codeBuilder.addStatement("$T $L = ($T) $T.class.newInstance()", element.getDefaultValue(), makerName, element.getDefaultValue(), element.getDefaultValue());
                    codeBuilder.addStatement("$N = ($T) $N.make($S)", element.getMethodName(), element.getReturnType(), makerName, element.getMethodName());
                    codeBuilder.endControlFlow();
                } else {
                    String makerName = element.getMethodName() + "Maker";
                    codeBuilder.addStatement("$T $L = ($T) $T.class.newInstance()", element.getDefaultValue(), makerName, element.getDefaultValue(), element.getDefaultValue());
                    codeBuilder.addStatement("$N = ($T) $N.make($S)", element.getMethodName(), element.getReturnType(), makerName, element.getMethodName());
                }
                codeBuilder.nextControlFlow("catch (InstantiationException | IllegalAccessException e)");
                codeBuilder.addStatement("throw new IllegalStateException(\"Cannot instantiate $T for $L because \"+e.getMessage())", element.getDefaultValue(), element.getMethodName());
                codeBuilder.endControlFlow();
            } else if (element.getPropertyType() == ADAPTER) {
                codeBuilder.addStatement("$T $N", element.getReturnType(), element.getMethodName());
                codeBuilder.beginControlFlow("try");
                codeBuilder.addStatement("$T adapter = ($T) $T.class.newInstance()", MapAdapter.class, MapAdapter.class, element.getAdapter());
                codeBuilder.addStatement("$N = ($T) adapter.fromMap($S, ($T) map.get($S))", element.getMethodName(), element.getReturnType() ,element.getMethodName(), element.getAdapterType(), element.getMapItemName());
                codeBuilder.nextControlFlow("catch (InstantiationException | IllegalAccessException e)");
                codeBuilder.addStatement("throw new IllegalStateException(\"Cannot instantiate adapter for $L because \"+e.getMessage())", element.getMethodName());
                codeBuilder.endControlFlow();
            } else if (element.getPropertyType() == ENUM) {
                codeBuilder.addStatement("$T $N = null", element.getReturnType(), element.getMethodName());
                codeBuilder.beginControlFlow("if (map.get($S) != null)", element.getMapItemName());
                codeBuilder.addStatement("$N = Enum.valueOf($T.class, map.get($S).toString())", element.getMethodName(), element.getReturnType(), element.getMapItemName());
                codeBuilder.endControlFlow();
            } else {
                codeBuilder.addStatement("$T $N = ($T) map.get($S)", element.getReturnType(), element.getMethodName(), element.getReturnType(), element.getMapItemName());
            }
        }

        builder.addCode(codeBuilder.build());
        builder.addCode("return ");
        builder.addCode(Utils.newFinalClassConstructorCall(context, names));

        return builder.build();
    }


    @Override
    public Set<ExecutableElement> consumeMethods(Context context) {
        Set<ExecutableElement> set = new HashSet<>();
        for (ExecutableElement element : context.abstractMethods()) {
            switch (element.getSimpleName().toString()) {
                case "toMap":
                    set.add(element);
                    break;
            }
        }
        return set;
    }

    private MethodSpec generateConstructor(List<MapElement> properties) {
        List<ParameterSpec> params = Lists.newArrayListWithCapacity(properties.size());
        for (MapElement element : properties) {
            params.add(ParameterSpec.builder(element.getReturnType(), element.getMethodName()).build());
        }

        MethodSpec.Builder builder = MethodSpec.constructorBuilder().addParameters(params);

        StringBuilder superFormat = new StringBuilder("super(");
        List<ParameterSpec> args = new ArrayList<>();
        for (int i = 0, n = params.size(); i < n; i++) {
            args.add(params.get(i));
            superFormat.append("$N");
            if (i < n - 1) superFormat.append(", ");
        }
        superFormat.append(")");
        builder.addStatement(superFormat.toString(), args.toArray());

        return builder.build();
    }
}
