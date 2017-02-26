package com.raybritton.autovaluemap;

import com.google.auto.value.extension.AutoValueExtension;
import com.google.auto.value.extension.AutoValueExtension.Context;
import com.google.auto.value.processor.AutoValueProcessor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.WARNING;

class Utils {
    static CodeBlock newFinalClassConstructorCall(Context context, Object[] properties) {
        CodeBlock constructorName = CodeBlock.of("new $T", getFinalClassClassName(context));
        return newConstructorCall(constructorName, properties);
    }

    private static CodeBlock newConstructorCall(CodeBlock constructorName, Object[] properties) {
        StringBuilder params = new StringBuilder("(");
        for (int i = properties.length; i > 0; i--) {
            params.append("$N");
            if (i > 1) params.append(", ");
        }
        params.append(")");
        return CodeBlock.builder()
                .add(constructorName)
                .addStatement(params.toString(), properties)
                .build();
    }

    private static String getFinalClassSimpleName(Context context) {
        TypeElement autoValueClass = context.autoValueClass();
        String name = autoValueClass.getSimpleName().toString();

        Element enclosingElement = autoValueClass.getEnclosingElement();
        while (enclosingElement instanceof TypeElement) {
            name = enclosingElement.getSimpleName().toString() + "_" + name;
            enclosingElement = enclosingElement.getEnclosingElement();
        }

        return "AutoValue_" + name;
    }

    private static ClassName getFinalClassClassName(Context context) {
        return ClassName.get(context.packageName(), getFinalClassSimpleName(context));
    }

    public static void error(Context context, String message, Element element) {
        context.processingEnvironment().getMessager().printMessage(ERROR, message, element);
    }

    public static void warning(Context context, String message, Element element) {
        context.processingEnvironment().getMessager().printMessage(WARNING, message, element);
    }
}
