package configgen.genjava.code;

import configgen.schema.FieldType;

import static configgen.schema.FieldType.Primitive.*;


class TypeStr {
    static boolean isLangSwitch = false;

    static String type(FieldType t) {
        return _type(t, false);
    }

    static String boxType(FieldType t) {
        return _type(t, true);
    }

    private static String _type(FieldType t, boolean box) {
        return switch (t) {
            case BOOL -> box ? "Boolean" : "boolean";
            case INT -> box ? "Integer" : "int";
            case LONG -> box ? "Long" : "long";
            case FLOAT -> box ? "Float" : "float";
            case STRING -> "String";
            case TEXT -> isLangSwitch ? STR. "\{ Name.codeTopPkg }.Text" : "String";
            case StructRef structRef -> Name.fullName(structRef.obj());
            case FList fList -> "java.util.List<" + _type(fList.item(), true) + ">";
            case FMap fMap -> "java.util.Map<" + _type(fMap.key(), true) + ", " + _type(fMap.value(), true) + ">";
        };
    }

    static String readValue(FieldType t) {
        return switch (t) {
            case BOOL -> "input.readBool()";
            case INT -> "input.readInt()";
            case LONG -> "input.readLong()";
            case FLOAT -> "input.readFloat()";
            case STRING -> "input.readStr()";
            case TEXT -> isLangSwitch ? STR. "\{ Name.codeTopPkg }.Text._create(input)" : "input.readStr()";
            case StructRef structRef -> STR. "\{ Name.fullName(structRef.obj()) }._create(input)" ;
            case FList _ -> null;
            case FMap _ -> null;
        };
    }

    static boolean isJavaPrimitive(FieldType t) {
        return switch (t) {
            case BOOL, INT, LONG, FLOAT -> true;
            default -> false;
        };
    }
}
