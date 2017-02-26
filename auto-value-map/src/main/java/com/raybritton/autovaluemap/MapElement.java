package com.raybritton.autovaluemap;

import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.MoreElements;
import com.google.auto.value.extension.AutoValueExtension;
import com.google.common.base.Optional;
import com.raybritton.autovaluemap.annotations.MapElementAdapter;
import com.raybritton.autovaluemap.annotations.MapElementName;
import com.raybritton.autovaluemap.annotations.MapEnum;
import com.raybritton.autovaluemap.annotations.MapHide;
import com.raybritton.autovaluemap.makers.BooleanArrayMaker;
import com.raybritton.autovaluemap.makers.BooleanMaker;
import com.raybritton.autovaluemap.makers.ByteArrayMaker;
import com.raybritton.autovaluemap.makers.ByteMaker;
import com.raybritton.autovaluemap.makers.CharArrayMaker;
import com.raybritton.autovaluemap.makers.CharMaker;
import com.raybritton.autovaluemap.makers.DoubleArrayMaker;
import com.raybritton.autovaluemap.makers.DoubleMaker;
import com.raybritton.autovaluemap.makers.FloatArrayMaker;
import com.raybritton.autovaluemap.makers.FloatMaker;
import com.raybritton.autovaluemap.makers.IntArrayMaker;
import com.raybritton.autovaluemap.makers.IntegerMaker;
import com.raybritton.autovaluemap.makers.LongArrayMaker;
import com.raybritton.autovaluemap.makers.LongMaker;
import com.raybritton.autovaluemap.makers.NullMaker;
import com.raybritton.autovaluemap.makers.ShortArrayMaker;
import com.raybritton.autovaluemap.makers.ShortMaker;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

class MapElement {
    enum Type {
        INT(IntegerMaker.class),
        LONG(LongMaker.class),
        SHORT(ShortMaker.class),
        BYTE(ByteMaker.class),
        FLOAT(FloatMaker.class),
        DOUBLE(DoubleMaker.class),
        CHAR(CharMaker.class),
        STRING(NullMaker.class),
        ENUM(NullMaker.class),
        BOOLEAN(BooleanMaker.class),
        ADAPTER(NullMaker.class),
        HIDDEN(NullMaker.class),
        BOOLEAN_ARRAY(BooleanArrayMaker.class),
        BYTE_ARRAY(ByteArrayMaker.class),
        INT_ARRAY(IntArrayMaker.class),
        CHAR_ARRAY(CharArrayMaker.class),
        SHORT_ARRAY(ShortArrayMaker.class),
        FLOAT_ARRAY(FloatArrayMaker.class),
        DOUBLE_ARRAY(DoubleArrayMaker.class),
        LONG_ARRAY(LongArrayMaker.class);

        final ClassName defaultValue;

        Type(Class val) {
            this.defaultValue = ClassName.get(val);
        }
    }

    private final Type type;
    private final String methodName;
    private final String mapItemName;
    private final ClassName adapter;
    private final ClassName adapterType;
    private final TypeName returnType;
    private String adapterFieldName;
    private final ClassName defaultValue;
    private final boolean readFromMap;

    private MapElement(Type type, String methodName, String mapItemName, ClassName adapter, ClassName adapterType, TypeName returnType, ClassName defaultValue, boolean readFromMap) {
        this.type = type;
        this.methodName = methodName;
        this.mapItemName = mapItemName;
        this.adapter = adapter;
        this.adapterType = adapterType;
        this.returnType = returnType;
        this.defaultValue = defaultValue;
        this.readFromMap = readFromMap;
    }

    static MapElement create(AutoValueExtension.Context context, ExecutableElement element) {
        Type type = null;
        ClassName adapter = null;
        ClassName adapterType = null;
        String mapName = null;
        TypeName typeName = TypeName.get(element.getReturnType());
        ClassName defaultValue = null;
        boolean readFromMap = false;
        if (element.getAnnotationsByType(MapHide.class).length > 0) {
            Optional<AnnotationMirror> optMirror = MoreElements.getAnnotationMirror(element, MapHide.class);
            if (optMirror.isPresent()) {
                TypeMirror valueMirror = (TypeMirror) AnnotationMirrors.getAnnotationValue(optMirror.get(), "value").getValue();
                readFromMap = (boolean) AnnotationMirrors.getAnnotationValue(optMirror.get(), "readFromMap").getValue();
                defaultValue = (ClassName) TypeName.get(valueMirror);
            }
            type = Type.HIDDEN;
        } else if (element.getAnnotationsByType(MapElementAdapter.class).length > 0) {
            type = Type.ADAPTER;
            Optional<AnnotationMirror> optMirror = MoreElements.getAnnotationMirror(element, MapElementAdapter.class);
            if (optMirror.isPresent()) {
                TypeMirror aMirror = (TypeMirror) AnnotationMirrors.getAnnotationValue(optMirror.get(), "adapter").getValue();
                TypeMirror tMirror = (TypeMirror) AnnotationMirrors.getAnnotationValue(optMirror.get(), "mapType").getValue();
                adapter = (ClassName) TypeName.get(aMirror);
                adapterType = (ClassName) TypeName.get(tMirror);
            }
        } else if (typeName.equals(TypeName.BOOLEAN) || typeName.equals(TypeName.BOOLEAN.box())) {
            type = Type.BOOLEAN;
        } else if (typeName.equals(TypeName.CHAR) || typeName.equals(TypeName.CHAR.box())) {
            type = Type.CHAR;
        } else if (typeName.equals(TypeName.BYTE) || typeName.equals(TypeName.BYTE.box())) {
            type = Type.BYTE;
        } else if (typeName.equals(TypeName.INT) || typeName.equals(TypeName.INT.box())) {
            type = Type.INT;
        } else if (typeName.equals(TypeName.LONG) || typeName.equals(TypeName.LONG.box())) {
            type = Type.LONG;
        } else if (typeName.equals(TypeName.SHORT) || typeName.equals(TypeName.SHORT.box())) {
            type = Type.SHORT;
        } else if (typeName.equals(TypeName.FLOAT) || typeName.equals(TypeName.FLOAT.box())) {
            type = Type.FLOAT;
        } else if (typeName.equals(TypeName.DOUBLE) || typeName.equals(TypeName.DOUBLE.box())) {
            type = Type.DOUBLE;
        } else if (typeName.equals(TypeName.get(String.class))) {
            type = Type.STRING;
        } else if (element.getReturnType().getKind() == TypeKind.DECLARED && element.getAnnotationsByType(MapEnum.class).length > 0) {
            type = Type.ENUM;
        } else if (element.getReturnType().getKind() == TypeKind.ARRAY) {
            ArrayType array = (ArrayType) element.asType();
            TypeName arrayType = TypeName.get(array.getComponentType());
            if (arrayType.equals(TypeName.BOOLEAN)) {
                type = Type.BOOLEAN_ARRAY;
            } else if (array.equals(TypeName.BYTE)) {
                type = Type.BYTE_ARRAY;
            } else if (array.equals(TypeName.CHAR)) {
                type = Type.CHAR_ARRAY;
            } else if (array.equals(TypeName.DOUBLE)) {
                type = Type.DOUBLE_ARRAY;
            } else if (array.equals(TypeName.FLOAT)) {
                type = Type.FLOAT_ARRAY;
            } else if (array.equals(TypeName.INT)) {
                type = Type.INT_ARRAY;
            } else if (array.equals(TypeName.LONG)) {
                type = Type.LONG_ARRAY;
            } else if (array.equals(TypeName.SHORT)) {
                type = Type.SHORT_ARRAY;
            }
        }

        MapElementName[] name = element.getAnnotationsByType(MapElementName.class);
        if (name.length > 0) {
            mapName = name[0].value();
        }

        if (type != null) {
            if (defaultValue == null) {
                defaultValue = type.defaultValue;
            }
            return new MapElement(type, element.getSimpleName().toString(), mapName, adapter, adapterType, typeName, defaultValue, readFromMap);
        } else {
            Utils.warning(context, "AutoValueMap does not support type", element);
            return null;
        }
    }

    Type getPropertyType() {
        return type;
    }

    String getMethodName() {
        return methodName;
    }

    TypeName getReturnType() {
        return returnType;
    }

    String getMapItemName() {
        if (mapItemName == null || mapItemName.isEmpty()) {
            return methodName;
        } else {
            return mapItemName;
        }
    }

    ClassName getAdapter() {
        return adapter;
    }

    ClassName getAdapterType() {
        return adapterType;
    }

    String getAdapterFieldName() {
        return adapterFieldName;
    }

    void setAdapterFieldName(String adapterFieldName) {
        this.adapterFieldName = adapterFieldName;
    }

    ClassName getDefaultValue() {
        return defaultValue;
    }

    public boolean isReadFromMap() {
        return readFromMap;
    }
}
